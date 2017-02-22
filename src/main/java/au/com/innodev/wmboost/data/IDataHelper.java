package au.com.innodev.wmboost.data;

import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IData;

class EntryUtil {

	public static Object normaliseValueForPut(Object value, ConversionService conversionService) {
		if (value instanceof Document) {
			return conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(IData.class));
		}
		else if (value instanceof Document[]) {
			return conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(IData[].class));
		}
		else if (value instanceof Iterable<?>) {
			if (CollectionUtil.areAllElementsOfType((Collection<?>) value, Document.class)) {
				return conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(IData[].class));
			}
			else {
				return value;
			}
			 
		}
		else {
			return value;
		}
	}
	
	public static <A> A normaliseValueForGet(A value, ConversionService conversionService) {
		if (value instanceof IData) {
			@SuppressWarnings("unchecked")
			A normalised = (A) conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(Document.class));
			return normalised;
		}		
		else if (value instanceof IData[]) {
			@SuppressWarnings("unchecked")
			A normalised = (A) conversionService.convert(value, TypeDescriptor.forObject(value), getDocListType());
			return normalised;
		}
		else if (value instanceof Object[]) {
			@SuppressWarnings("unchecked")
			A normalised = (A) conversionService.convert(value, TypeDescriptor.forObject(value), getObjectListType());
			return normalised;
		}
		else {
			return value;
		}
	}
	
	private static TypeDescriptor getObjectListType() {
		TypeDescriptor docListType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Object.class));
		return docListType;
	}
	
	private static TypeDescriptor getDocListType() {
		TypeDescriptor docListType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Document.class));
		return docListType;
	}
}
