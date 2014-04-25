package rodrigo.floatview.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * @author fanxiangchao
 * @date 2013-6-17 13:03
 */
public class ViewMappingUtil {
	public static void mapView(Object object, View rootView) {
		startReflect(object, rootView, object.getClass());
	}

	public static void mapView(Object object, View rootView, int layer) {
		startReflect(object, rootView, object.getClass());
		Class<?> clazz = object.getClass().getSuperclass();
		for (int i = 0; i < layer - 1; i++, clazz = clazz.getSuperclass()) {
			startReflect(object, rootView, clazz);
		}
	}

	private static void startReflect(Object object, View rootView, Class<?> clazz) {
		Context context = rootView.getContext();
		Field[] fields = clazz.getDeclaredFields();
		ViewMapping viewMapping;
		for (Field field : fields) {
			viewMapping = field.getAnnotation(ViewMapping.class);
			String str_ID;
			String type;
			if (viewMapping != null) {
				try {
					str_ID = viewMapping.str_ID();
					type = viewMapping.type();
					field.setAccessible(true);
					//LIYAN 兼容取代覆盖
					View temp = rootView.findViewById(context.getResources().getIdentifier(str_ID, type, context.getPackageName()));
					if (temp != null) {
						field.set(object, temp);
					}
				} catch (Exception e) {
					throw new RuntimeException(viewMapping.str_ID() + " map error!");
				}
			}
		}
	}

	/**
	 * @param object
	 * @param activity
	 * @description 用于在类上映射layout
	 */
	public static void mapView(Object object, Activity activity) {
		Context context = activity;
		ViewMapping viewMapping = object.getClass().getAnnotation(ViewMapping.class);
		View rootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
		  activity.getResources().getIdentifier(viewMapping.str_ID(),
			viewMapping.type(), activity.getPackageName()), null);
		activity.setContentView(rootView);
		mapView(object, rootView);
	}

	public static View mapView(Object object, Context context) {
		return mapView(object, context, null, false);
	}

	public static View mapView(Object object, Context context, ViewGroup parent, boolean isAttach) {
		ViewMapping viewMapping = object.getClass().getAnnotation(ViewMapping.class);
		View rootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
		  context.getResources().getIdentifier(viewMapping.str_ID(),
			viewMapping.type(), context.getPackageName()), parent, isAttach);
		mapView(object, rootView);
		return rootView;
	}

}
