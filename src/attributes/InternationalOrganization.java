package attributes;

import interfaces.Organization;

public class InternationalOrganization extends OrganizationDecorator{
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6837078219316835280L;

	public InternationalOrganization(Organization o) {
		super(o);
	}

	@Override
	public String selection() {
		// TODO Auto-generated method stub
		return super.selection() + " Can be an exchange student.";
	}

	@Override
	public String getBonus() {
		// TODO Auto-generated method stub
		return super.getBonus() + " International level certificate.";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}
	
	
}
