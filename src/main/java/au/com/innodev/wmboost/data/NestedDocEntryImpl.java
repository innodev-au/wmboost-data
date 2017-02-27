package au.com.innodev.wmboost.data;

import static au.com.innodev.wmboost.data.NormaliseOption.DONT_NORMALISE;

import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IData;

class NestedDocEntryImpl extends ItemEntryImpl<Document> implements NestedDocEntry {

	private final DocumentFactory documentFactory;

	public NestedDocEntryImpl(DocumentImpl document, DocumentFactory factory, String key) {
		super(document, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class), DONT_NORMALISE);
		this.documentFactory = factory;
	}
	
	public Document putNew() {
		Document document = documentFactory.create();
		put(document);
		
		return document;
	}
}
