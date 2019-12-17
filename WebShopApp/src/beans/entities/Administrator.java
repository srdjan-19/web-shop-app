package beans.entities;

import java.io.Serializable;
import java.util.Date;

import beans.enums.Roles;

public class Administrator extends User implements Serializable {

	private static final long serialVersionUID = -4802560474084055884L;

	public Administrator() {
		this.role = Roles.ADMIN;
		this.registered = formatDate(new Date());

	}

	public Administrator(String un, String pw, String fn, String ln, String p, String e, String rd) {
		this.username = un;
		this.password = pw;
		this.firstName = fn;
		this.lastName = ln;
		this.phone = p;
		this.email = e;
		this.role = Roles.ADMIN;
		this.registered = rd;
	}

}
