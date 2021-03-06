package org.dylan.chinesepinyin.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dylan.chinesepinyin.dict.ResourceConfig;
import org.dylan.chinesepinyin.dict.ResourceTool;
import org.dylan.chinesepinyin.dict.ResourceType;
import org.dylan.chinesepinyin.sort.PinyinComparator;
import org.dylan.chinesepinyin.util.Utils;
import org.junit.Test;

/**
 * Just for Test
 * 
 * @author dylan.zhang (Dylan.zhangzhi@gmail.com)
 * 
 */
public class MainTest {
	ResourceConfig dHelp = ResourceTool.getInstance();
	ResourceTool resourceTool = ResourceTool.getInstance();
	Utils utils = Utils.getInstance();

	@Test
	public void loadResource() {
		// System.out.println(dHelp.loadAllDataOfSoundMark());
		System.out.println(dHelp.loadResource(ResourceType.LOADPINYINMAP));
	}

	@Test
	public void getPinYinMap() {
		resourceTool.getPinYinSource();
	}

	@Test
	public void findSimplyPinYinWithHanZi() throws UnsupportedEncodingException {
		System.out.println(resourceTool.findSingtonPinYinByHanZi("祖國",
				ResourceType.OutPutStyle.YUNMU));
		System.out.println(resourceTool.findSingtonPinYinByHanZi(
				"天 朝,hello World", null));
		System.out.println(resourceTool.findSingtonPinYinByHanZi(
				"今天天气真心不错！ Happy!", ResourceType.OutPutStyle.NOTHING));
		System.out.println(resourceTool.findSingtonPinYinByHanZi(
				"今天天气真心不错！ Happy!", ResourceType.OutPutStyle.SHENGMU));
		System.out.println(resourceTool.findSingtonPinYinByHanZi("情绪是条河流！",
				ResourceType.OutPutStyle.WITHTONE));
		System.out.println(resourceTool.findSingtonPinYinByHanZi(
				"12345，上山打老虎！", ResourceType.OutPutStyle.NUMBER));

		/**
		 * Don't do this: new String("天 朝,hello World".getBytes("utf-8")); it
		 * will output "chan ?lian ?hello World" not "tian  zhao ,hello World";
		 * 
		 * Please see @{code : loadResource}
		 */
	}

	@Test
	public void handleType() {
		System.out.println("helloworld,guo".replaceAll("[0-9]", ""));
		System.out.println(resourceTool.handleType("guó ",
				ResourceType.OutPutStyle.NUMBER));
	}

	@Test
	public void toPinYinWithMap() {
//		Map<String, String[]> map = resourceTool.toPinYinWithMap(
//				"天气真不错，真不错，happy!", ResourceType.OutPutStyle.WITHTONE);
		Map<String, String[]> map = resourceTool.toPinYinWithMapMixModul(
				"天气真不错，真不错，happy!");
		Collection<String[]> value = map.values();
		Iterator<String[]> it = value.iterator();
		for (; it.hasNext();) {
			for (String temp : it.next()) {
				System.out.print(temp + "   ");
			}

		}
		System.out.println();
		Set<String> setKey = map.keySet();
		Iterator<String> keyIt = setKey.iterator();
		while(keyIt.hasNext()){
			String key= keyIt.next();
			String[] value1 = map.get(key);
			System.out.println(key+" ");
			for(String flag:value1){
				System.out.println("   "+flag);
			}
		}
	}
	
	@Test
	public void toStringWith(){
		String str1="今天天气真心不错！ Happy!";
		System.out.println(resourceTool.toStringWith(str1,"_"));
		
		String[] arr1 = { "张三", "李四", "王二", "麻子", "Android", "10086", "@%~*&^#$", "hello world", "怡情" }; 
		System.out.println(resourceTool.toStringWith(arr1));  
		
		List<String> list = Arrays.asList(arr1); 
		System.out.println(resourceTool.toStringWith(list)); 
	}
	@Test public void toPinYinWithStringArray(){
		//String[] arr1 = { "张三", "李四", "王二", "麻子", "Android", "10086", "@%~*&^#$", "hello world", "怡情" }; 
		System.out.println(resourceTool.toPinYinWithStringArray('会',Utils.PinYinStyles.COMPLETE)[1]);  
		System.out.println(resourceTool.toPinYinWithString('@',ResourceType.OutPutStyle.WITHTONE,Utils.PinYinStyles.COMPLETE)); 
		System.out.println(resourceTool.toPinYinWithString('会',ResourceType.OutPutStyle.WITHTONE,Utils.PinYinStyles.ONLYCHINEASE)); 
		System.out.println(resourceTool.toPinYinWithString(' ',ResourceType.OutPutStyle.WITHTONE,Utils.PinYinStyles.COMPLETE)); 
		System.out.println(resourceTool.toPinYinWithString('？',ResourceType.OutPutStyle.WITHTONE,Utils.PinYinStyles.ONLYCHINEASE)); 
		/**
		 * resourceTool.toPinYinWithStringArray('会',ResourceType.OutPutStyle.WITHTONE);
		 * resourceTool.toPinYinWithStringArray('?');
		 */
		/**
		 * resourceTool.toPinYinWithStringArray('a',Utils.PinYinStyles.ONLYCHINEASE)  ==> null
		 * resourceTool.toPinYinWithStringArray('a',Utils.PinYinStyles.COMPLETE)  ==> a
		 * resourceTool.toPinYinWithStringArray('会',Utils.PinYinStyles.COMPLETE) ==> hui   kuai 
		 * resourceTool.toPinYinWithStringArray('会',Utils.PinYinStyles.ONLYCHINEASE) ==> hui   kuai 
		 */
		String[] array = resourceTool.toPinYinWithStringArray('会',Utils.PinYinStyles.ONLYCHINEASE);
		for(String temp:array){
			System.out.print(temp + "   ");
		}
	}
	
	@Test
	public void arraySort() {
		Comparator<String> comparator = new PinyinComparator();
		String[] arr = { "张三", "a","1@","李四","1111111","abc" ,"王二","張三" ,"麻子", "Android", "10086",
				"@%~*&^#$", "hello world", "A","张大","怡情" };
		//System.out.println(resourceTool.toStringWith(arr));
		//System.out.println(concatPinyinStringArray(arr));
		Arrays.sort(arr,comparator);
		//resourceTool.toStringWith(arr," , ");
		System.out.println(resourceTool.toStringWith(arr," , "));
		System.out.println(comparator.compare("b", "2"));
		System.out.println(comparator.compare("張三", "李四"));
		System.out.println(comparator.compare("w", "b"));
		System.out.println(comparator.compare("2", "-1"));
		
	}

	@Test public void listSort(){
		Comparator<String> comparator = new PinyinComparator();
		
		String[] arr1 = { "张三", "a","1@","李四","1111111","abc" ,"王二","張三" ,"麻子", "Android", "10086",
				"@%~*&^#$", "hello world", "A","张大","怡情" };
		//System.out.println(resourceTool.toStringWith(arr));
		//System.out.println(concatPinyinStringArray(arr));
		List<String> arr=Arrays.asList(arr1);
		Collections.sort(arr, comparator);
		//Arrays.sort(arr,comparator);
		//resourceTool.toStringWith(arr," , ");
		System.out.println(resourceTool.toStringWith(arr," , "));
		System.out.println(comparator.compare("b", "2"));
		System.out.println(comparator.compare("張三", "李四"));
		System.out.println(comparator.compare("w", "b"));
		System.out.println(comparator.compare("2", "-1"));
	}
}
