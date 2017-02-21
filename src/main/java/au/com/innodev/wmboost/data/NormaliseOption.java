package au.com.innodev.wmboost.data;

/**
 * Option for whether to normalise a value to another. For example, Document is
 * normalised to IData when storing and vice-versa when retrieving.
 */
enum NormaliseOption {

	MAY_NORMALISE, DONT_NORMALISE;

	public boolean isDontNormalise() {
		return this == DONT_NORMALISE;
	}
}
