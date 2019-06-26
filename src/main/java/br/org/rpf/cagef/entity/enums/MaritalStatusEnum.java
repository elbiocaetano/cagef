package br.org.rpf.cagef.entity.enums;

public enum MaritalStatusEnum {

	SOLTEIRO("solteiro"), CASADO("casado"), DIVORCIADO("divorciado"), VIUVO("viuvo");

	private String description;

	private MaritalStatusEnum(String description) {
		this.description = description;
	}

	public static MaritalStatusEnum toEnum(String description) {

		if (description == null) {
			return null;
		}

		for (MaritalStatusEnum x : MaritalStatusEnum.values()) {
			if (description.equals(x.getDescription())) {
				return x;
			}
		}

		throw new IllegalArgumentException("Invalid description: " + description);
	}

	public String getDescription() {
		return description;
	}
}
