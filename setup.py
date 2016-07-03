import os
import resort
import resort.component
import resort.component.glassfish
import resort.component.maven
import resort.component.packer
import resort.component.postgresql
import resort.component.resource
import resort.component.vagrant

class LocalProfile:

	def __init__(self):
	
		self.__box_file = None
		self.__vagrant_inst_app = None
		self.__vagrant_inst_db = None
		self.__db_conn = None
		self.__domain_app = None
		self.__props = {}
		
	def __prepare_prop(self, props, key, value):
	
		try:
			self.__props[key] = props[key]
		except KeyError:
			self.__props[key] = value
			
	def __layout_path(self, path):
	
		return lambda ctx_props: os.path.join(os.path.join(
				ctx_props["base_dir"], "layout"), path)
				
	def __module_path(self, path):
	
		return lambda ctx_props: os.path.join(os.path.join(
				ctx_props["base_dir"], "module"), path)
				
	def __profile_path(self, path):
	
		return lambda ctx_props: os.path.join(ctx_props["profile_dir"], path)
				
	def __artifact_path(self, artifact_id, version, package):
	
		path = os.path.join(
			artifact_id,
			os.path.join("target", "{}-{}.{}".format(
				artifact_id,
				version,
				package
			)
		))
		return lambda ctx_props: os.path.join(ctx_props["base_dir"],
				self.__module_path(path)(ctx_props))
				
	def prepare(self, props):
	
		self.__prepare_prop(props, "machine_app_host", "192.168.50.20")
		self.__prepare_prop(props, "machine_db_host", "192.168.50.40")
		
		self.__prepare_prop(props, "app_mail_host", "<empty>")
		self.__prepare_prop(props, "app_mail_port", "<empty>")
		self.__prepare_prop(props, "app_mail_username", "<empty>")
		self.__prepare_prop(props, "app_mail_password", "<empty>")
		self.__prepare_prop(props, "app_mail_from", "<empty>")
		self.__prepare_prop(props, "app_mail_auth", "true")
		self.__prepare_prop(props, "app_mail_starttls_enable", "true")
		
		self.__prepare_prop(props, "app_google_client_id", "<empty>")
		self.__prepare_prop(props, "app_google_client_secret", "<empty>")
		self.__prepare_prop(props, "app_google_application_name", "<empty>")
		self.__prepare_prop(props, "app_google_credential", "<empty>")
		self.__prepare_prop(props, "app_google_drive_folder", "<empty>")
		
		self.__box_file = resort.component.vagrant.BoxFile(
				lambda ctx_props: "pto-{}/debian".format(
				ctx_props["profile_name"]),
				lambda ctx_props: os.path.join(ctx_props["profile_dir"],
				"image"))
				
		self.__vagrant_inst_app = resort.component.vagrant.Instance(
				lambda ctx_props: ctx_props["profile_dir"],
				lambda ctx_props: "pto-{}-app".format(
				ctx_props["profile_name"]))
		self.__vagrant_inst_db = resort.component.vagrant.Instance(
				lambda ctx_props: ctx_props["profile_dir"],
				lambda ctx_props: "pto-{}-db".format(
				ctx_props["profile_name"]))
				
		self.__db_conn = resort.component.postgresql.Connection(
			self.__props["machine_db_host"],
			5432,
			"ptodb",
			"admin",
			"12345678"
		)
		
		self.__domain_app = resort.component.glassfish.domain(
			self.__props["machine_app_host"],
			5838,
			"admin",
			"12345678"
		)
		
		return self.__props
			
	def dependencies(self, comp_name):
	
		if comp_name != "component":
			yield "component"
			
		if comp_name is None:
			yield "app"
		elif comp_name == "app":
			yield "appmaweb"
			yield "appcertweb"
			yield "appcore"
			yield "filestgra"
			yield "extuidra"
		elif comp_name == "appmaweb":
			yield "appserv-app-running"
			yield "module-appmaweb"
			yield "app-settings"
			yield "appcore"
		elif comp_name == "appcertweb":
			yield "appserv-app-running"
			yield "google-drive-filestg-resource"
			yield "module-appcertweb"
		elif comp_name == "appcore":
			yield "appserv-app-running"
			yield "machine-db-running"
			yield "module-appcore"
			yield "app-settings"
			yield "app-mail-session"
			yield "google-extid-resource"
			yield "app-datasource-resource"
			yield "realm-datasource-resource"
			yield "google-drive-filestg-resource"
		elif comp_name == "filestgra":
			yield "appserv-app-running"
			yield "module-filestgra"
		elif comp_name == "extuidra":
			yield "appserv-app-running"
			yield "machine-db-running"
			yield "module-extuidra"
		elif comp_name == "app-settings":
			yield "appserv-app-running"
		elif comp_name == "app-mail-session":
			yield "appserv-app-running"
		elif comp_name == "google-drive-filestg-resource":
			yield "google-drive-filestg-pool"
			yield "appserv-app-running"
		elif comp_name == "google-extid-resource":
			yield "google-extid-pool"
			yield "appserv-app-running"
		elif comp_name == "app-datasource-resource":
			yield "app-datasource-pool"
			yield "appserv-app-running"
		elif comp_name == "realm-datasource-resource":
			yield "app-datasource-pool"
			yield "appserv-app-running"
		elif comp_name == "google-drive-filestg-pool":
			yield "filestgra"
		elif comp_name == "google-extid-pool":
			yield "extuidra"
		elif comp_name == "app-datasource-pool":
			yield "appserv-app-running"
			yield "db-init"
		elif comp_name == "appserv-app-running":
			yield "machine-app-running"
		elif comp_name == "machine-app-running":
			yield "machine-app"
		elif comp_name == "machine-db-running":
			yield "machine-db"
		elif comp_name in ( "machine-app", "machine-db" ):
			yield "box-debian"
			yield "resources-machine"
			if comp_name == "machine-app":
				yield "module-realm"
				yield "module-realmdbsql"
				yield "module-realmasgf"
		elif comp_name == "box-debian":
			yield "image-debian"
		elif comp_name == "image-debian":
			yield "resources-image"
		elif comp_name.startswith("module-") and comp_name != "module-proj":
			yield "module-proj"
			if comp_name == "module-faces":
				yield "module-filestg"
			if comp_name == "module-appdom":
				yield "module-filestg"
				yield "module-geoloc"
			elif comp_name == "module-realmdbsql":
				yield "module-realm"
			elif comp_name == "module-realmasgf":
				yield "module-realmdbsql"
			elif comp_name == "module-appmaweb":
				yield "module-appdom"
				yield "module-extuid"
				yield "module-faces"
			elif comp_name == "module-appcore":
				yield "module-appdom"
				yield "module-extuid"
			elif comp_name == "module-filestgra":
				yield "module-rau"
				yield "module-filestg"
			elif comp_name == "module-extuidra":
				yield "module-rau"
				yield "module-extuid"
		elif comp_name == "db-init":
			yield "machine-db-running"
			
	def component(self, comp_name):
	
		if comp_name == "box-debian":
			return self.__box_file
		if comp_name == "resources-image":
			return resort.component.resource.Set(
				self.__layout_path("image"),
				self.__profile_path("image"),
				{
					"admin_username": "vagrant",
					"admin_password": "vagrant"
				}
			)
		if comp_name == "resources-machine":
			return resort.component.resource.Set(
				self.__layout_path("Vagrantfile"),
				self.__profile_path("Vagrantfile"),
				lambda ctx_props: {
					"module_dir": self.__module_path("")(ctx_props),
					"layout_dir": self.__layout_path("")(ctx_props),
					"profile_name": ctx_props["profile_name"],
					"machine_app_host": self.__props["machine_app_host"],
					"machine_db_host": self.__props["machine_db_host"]
				}
			)
		if comp_name == "image-debian":
			return resort.component.packer.Image(self.__profile_path("image"),
					"debian.json")
		if comp_name == "machine-app":
			return self.__vagrant_inst_app.created()
		if comp_name == "machine-db":
			return self.__vagrant_inst_db.created()
		if comp_name == "machine-app-running":
			return self.__vagrant_inst_app.running()
		if comp_name == "machine-db-running":
			return self.__vagrant_inst_db.running()
		if comp_name == "module-proj":
			return resort.component.maven.Project(
					self.__module_path("ptoproj"), True)
		if comp_name == "module-realm":
			return resort.component.maven.Project(
					self.__module_path("ptorealm"), True)
		if comp_name == "module-realmdbsql":
			return resort.component.maven.Project(
					self.__module_path("ptorealmdbsql"), True)
		if comp_name == "module-realmasgf":
			return resort.component.maven.Project(
					self.__module_path("ptorealmasgf"))
		if comp_name == "module-faces":
			return resort.component.maven.Project(
					self.__module_path("ptofaces"), True)
		if comp_name == "module-rau":
			return resort.component.maven.Project(
					self.__module_path("ptorau"), True)
		if comp_name == "module-filestg":
			return resort.component.maven.Project(
					self.__module_path("ptofilestg"), True)
		if comp_name == "module-geoloc":
			return resort.component.maven.Project(
					self.__module_path("ptogeoloc"), True)
		if comp_name == "module-extuid":
			return resort.component.maven.Project(
					self.__module_path("ptoextuid"), True)
		if comp_name == "module-filestgra":
			return resort.component.maven.Project(
					self.__module_path("ptofilestgra"), False)
		if comp_name == "module-extuidra":
			return resort.component.maven.Project(
					self.__module_path("ptoextuidra"), False)
		if comp_name == "module-appdom":
			return resort.component.maven.Project(
					self.__module_path("ptoappdom"), True)
		if comp_name == "module-appmaweb":
			return resort.component.maven.Project(
					self.__module_path("ptoappmaweb"), False)
		if comp_name == "module-appcertweb":
			return resort.component.maven.Project(
					self.__module_path("ptoappcertweb"), False)
		if comp_name == "module-appcore":
			return resort.component.maven.Project(
					self.__module_path("ptoappcore"), False)
		if comp_name == "db-init":
			return self.__db_conn.database_changes(
					self.__layout_path(os.path.join(os.path.join("db",
					"ptoapp"), "init.sql")))
		if comp_name == "appserv-app-running":
			return self.__domain_app
		if comp_name == "appmaweb":
			return self.__domain_app.application(
				"ptoappmaweb",
				"/member",
				self.__artifact_path(
					"ptoappmaweb",
					"0.1.0-SNAPSHOT",
					"war"
				)
			)
		if comp_name == "appcertweb":
			return self.__domain_app.application(
				"ptoappcertweb",
				"/certificate",
				self.__artifact_path(
					"ptoappcertweb",
					"0.1.0-SNAPSHOT",
					"war"
				)
			)
		if comp_name == "appcore":
			return self.__domain_app.application(
				"ptoappcore",
				None,
				self.__artifact_path(
					"ptoappcore",
					"0.1.0-SNAPSHOT",
					"jar"
				)
			)
		if comp_name == "filestgra":
			return self.__domain_app.application(
				"ptofilestgra",
				None,
				self.__artifact_path(
					"ptofilestgra",
					"0.1.0-SNAPSHOT",
					"rar"
				)
			)
		if comp_name == "extuidra":
			return self.__domain_app.application(
				"ptoextuidra",
				None,
				self.__artifact_path(
					"ptoextuidra",
					"0.1.0-SNAPSHOT",
					"rar"
				)
			)
		if comp_name == "app-settings":
			return self.__domain_app.custom_resource(
				"properties/PTOSettings",
				"java.util.Properties",
				"org.glassfish.resources.custom.factory.PropertiesFactory",
				{
					"mailPassword": self.__props["app_mail_password"],
					"googleClientId": self.__props["app_google_client_id"]
				}
			)
		if comp_name == "app-mail-session":
			return self.__domain_app.mail_session(
				"mail/PTOMailSession",
				self.__props["app_mail_host"],
				self.__props["app_mail_username"],
				self.__props["app_mail_from"],
				{
					"mail.smtp.host": self.__props["app_mail_host"],
					"mail.smtp.port": self.__props["app_mail_port"],
					"mail.smtp.auth": self.__props["app_mail_auth"],
					"mail.smtp.starttls.enable":
							self.__props["app_mail_starttls_enable"]
				}
			)
		if comp_name == "google-extid-resource":
			return self.__domain_app.connector_resource(
				"extuid/PTOGoogleExternalID",
				"PTOGoogleExternalID"
			)
		if comp_name == "google-extid-pool":
			return self.__domain_app.connector_connection_pool(
				"PTOGoogleExternalID",
				"ptoextuidra",
				"net.preparatusopos.resource.security.external.google."
				"GoogleExternalUIDConnectionFactory",
				{
					"clientID": self.__props["app_google_client_id"],
					"clientSecret": self.__props["app_google_client_secret"]
				}
			)
		if comp_name == "google-drive-filestg-resource":
			return self.__domain_app.connector_resource(
				"file/PTOGoogleDriveFileStorage",
				"PTOGoogleDriveFileStorage"
			)
		if comp_name == "google-drive-filestg-pool":
			return self.__domain_app.connector_connection_pool(
				"PTOGoogleDriveFileStorage",
				"ptofilestgra",
				"net.preparatusopos.resource.tools.file.google.drive."
				"GoogleDriveFileStorageConnectionFactory",
				{
					"applicationName":
							self.__props["app_google_application_name"],
					"folderName": self.__props["app_google_drive_folder"],
					"credential": self.__props["app_google_credential"]
				}
			)
		if comp_name == "app-datasource-resource":
			return self.__domain_app.jdbc_resource(
				"jdbc/PTOApplicationDataSource",
				"PTOApplicationDataSource"
			)
		if comp_name == "realm-datasource-resource":
			return self.__domain_app.jdbc_resource(
				"jdbc/PTORealmDataSource",
				"PTOApplicationDataSource"
			)
		if comp_name == "app-datasource-pool":
			return self.__domain_app.jdbc_connection_pool(
				"PTOApplicationDataSource",
				"javax.sql.XADataSource",
				"org.postgresql.xa.PGXADataSource",
				{
					"user": "ptoapp",
					"password": "12345678",
					"portNumber": 5432,
					"databaseName": "ptodb",
					"serverName": self.__props["machine_db_host"]
				}
			)
		return None
		
resort.setup(
	work_dir="work",
	profiles={
		"local": LocalProfile()
	}
)

