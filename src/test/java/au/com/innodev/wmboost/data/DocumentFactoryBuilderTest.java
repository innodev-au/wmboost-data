package au.com.innodev.wmboost.data;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

import com.wm.data.IData;
import com.wm.util.Values;

public class DocumentFactoryBuilderTest {

	private static class MyClass{
		private String text;
		public MyClass(String text) {
			this.text = text;
		}
		public String getText() {
			return text;
		}
	};
	
	/*
	 * Creates a custom DocumentFactory with its own conversion service and direct data factory. 
	 * 
	 */
	@Test
	public void testCustomDocFactory() {
		final Values customIData =  new Values(new Object[][] {{"key1", "Hello!"}});
		
		GenericConversionService conversionService = new GenericConversionService();
		Converter<String, MyClass> converter = new Converter<String, MyClass>() {
			public MyClass convert(String source) {
				return new MyClass(source);
			};
		};
		conversionService.addConverter(String.class, MyClass.class, converter );
		DocumentFactoryBuilder docFactBuilder = new DocumentFactoryBuilder();
		docFactBuilder.setConversionService(conversionService);
		docFactBuilder.setDirectIDataFactory(new DirectIDataFactory() {
			
			@Override
			public IData create() {				
				return customIData;
			}
		});
		
		DocumentFactory docFact = docFactBuilder.build();
		Document document = docFact.create();
		
		// Confirm custom IData is the one that has been created
		assertSame(document.getIData(), customIData);
		
		// Confirm the conversion service we created is used
		assertEquals("Hello!", document.entry("key1", MyClass.class).getVal().getText());
		
	}

}
