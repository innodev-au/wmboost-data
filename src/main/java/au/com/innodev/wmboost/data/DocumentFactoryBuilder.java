package au.com.innodev.wmboost.data;

import org.springframework.core.convert.ConversionService;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * Builds a {@link DocumentFactory}.
 * 
 */
public class DocumentFactoryBuilder {

	private ConversionService conversionService;
	private DirectIDataFactory directIDataFactory;	

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public void setDirectIDataFactory(DirectIDataFactory directIDataFactory) {
		this.directIDataFactory = directIDataFactory;
	}

	public DocumentFactory build()
	{
		Preconditions.checkNotNull(conversionService, "ConversionService cannot be null");
		Preconditions.checkNotNull(directIDataFactory, "directIDataFactory cannot be null");
				
		return new DefaultDocumentFactory(conversionService, directIDataFactory);
	}
	
	
}
