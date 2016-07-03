package net.preparatusopos.app.domain;

import java.io.Serializable;

public interface Referenced<T>
extends Serializable
{
	public T getRef();
}
