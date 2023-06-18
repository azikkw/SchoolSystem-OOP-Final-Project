package attributes;

import interfaces.Organization;

public class InterUniversityOrganization extends InternationalOrganization{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3791282534386960014L;


	public InterUniversityOrganization(Organization o) {
		super(o);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}

	@Override
	public String selection() {
		// TODO Auto-generated method stub
		return super.selection() + " Must go through the selection process.";
	}


	@Override
	public String getBonus() {
		// TODO Auto-generated method stub
		return super.getBonus() + " Knowledge from multiple sources.";
	}
	
}
