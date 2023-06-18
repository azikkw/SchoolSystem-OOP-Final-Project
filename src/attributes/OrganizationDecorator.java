package attributes;

import java.io.Serializable;
import java.util.Vector;

import interfaces.Organization;
import users.Student;

public abstract class OrganizationDecorator implements Organization, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6793326377815414594L;
	public String name;
	public Vector<Student> list = new Vector<>();
	protected final Organization decoratedOrganization;
	
	public OrganizationDecorator(Organization o) {
		this.decoratedOrganization = o;
	}

	@Override
	public String selection() {
		// TODO Auto-generated method stub
		return this.decoratedOrganization.selection();
	}

	@Override
	public String getBonus() {
		// TODO Auto-generated method stub
		return this.decoratedOrganization.getBonus();
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	
}
