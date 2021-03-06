/*
 *  This file is part of chinesepinyin and distributed under 
 *  Apache Software Foundation (ASF).
 *  
 *  The chinesepinyin is a free software; you can redistribute it and/or modify 
 *  it under the terms of Apache Software Foundation (ASF).
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dylan.chinesepinyin.dict;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.dylan.chinesepinyin.test.MainTest;
import org.dylan.chinesepinyin.util.ChinesePinYinException;
import org.dylan.chinesepinyin.util.Utils.PinYinStyles;

/**
 * Extends {@code ResourceConfig}
 * 
 * @author dylan.zhang (Dylan.zhangzhi@gmail.com)
 * 
 */
public class ResourceTool extends ResourceConfig {
	private final JSONObject pinYinJsonObject = getPinYinSource();
	private final JSONObject soundMarkJsonObject = getSoundMarkSource();
	private final String[] SHENGMU = super.SHENGMU.split(",");

	// private final String[] YUNMU = super.YUNMU.split(",");

	private static class ResourceToolHolder {
		private static final ResourceTool INSTANCE = new ResourceTool();
	}

	private ResourceTool() {
	}

	public static final ResourceTool getInstance() {
		return ResourceToolHolder.INSTANCE;
	}

	private enum PinyinStyle {
		POLYPHONE, SINGTON;
	}
	
	/**
	 * Encapsulation pinyin_dict_map.js
	 * 
	 * @return JSONObject
	 */
	@Override
	public JSONObject getPinYinSource() {
		String pinYin = super.loadResource(ResourceType.LOADPINYINMAP);
		if (pinYin.equals(ResourceType.LOADRESOURCEFAIL.name())) {
			throw new ChinesePinYinException(
					"LOAD RESOURCE (pinyin_dict) FAIL !");
		}
		JSONObject jsonObject = JSONObject.fromObject(pinYin);
		pinYin = null;
		return jsonObject;
	}

	/**
	 * 
	 * Encapsulation sound_mark.js
	 * 
	 * @return JSONObject
	 */
	@Override
	public JSONObject getSoundMarkSource() {
		String soundMark = super.loadResource(ResourceType.LOADSOUNMARK);
		if (soundMark.equals(ResourceType.LOADRESOURCEFAIL.name())) {
			throw new ChinesePinYinException(
					"LOAD RESOURCE (sound_mark) FAIL !");
		}
		JSONObject jsonObject = JSONObject.fromObject(soundMark);
		soundMark = null;
		return jsonObject;
	}

	/**
	 * Get the Key-Set
	 * 
	 * @param resourceType
	 * @return the Key-Set
	 */
	public Set<?> returnPinYinKeys(ResourceType resourceType) {
		JSONObject jsonObject = null;
		switch (resourceType) {
		case LOADPINYINMAP:
			jsonObject = getPinYinSource();
			break;
		case LOADSOUNMARK:
			jsonObject = getSoundMarkSource();
			break;
		default:
			throw new ChinesePinYinException("Nonsupport 'ResourceType !'");
		}
		return jsonObject.keySet();
	}

	/**
	 * Most of time,please use this method; More details information please see
	 * {@link MainTest}
	 * 
	 * @param inputSource
	 * @param styles
	 * @return String
	 */
	public String findSingtonPinYinByHanZi(String inputSource,
			ResourceType.OutPutStyle styles) {
		int length = inputSource.length();

		StringBuilder sb = new StringBuilder();
		String tempString = null;
		String toneString = "";

		for (int i = 0; i < length; i++) {
			String c = inputSource.substring(i, i + 1);
			tempString = (String) pinYinJsonObject.get(c);
			if (pinYinJsonObject.containsKey(c)) {
				toneString = tempString.split(",")[0];
				sb.append(handleType(toneString, styles)).append(" ");
			} else {
				sb.append(c);
			}
		}
		String pinYin = sb.toString();
		tempString = null;
		return pinYin;
	}

	/**
	 * Input 'Conllection<String>' type parameter,output 'String' data<br/>
	 * 
	 * <pre>
	 * String[] arr1 = { &quot;张三&quot;, &quot;李四&quot;, &quot;王二&quot;, &quot;麻子&quot;, &quot;Android&quot;, &quot;10086&quot;, &quot;@%&tilde;*&amp;&circ;#$&quot;,
	 * 		&quot;hello world&quot;, &quot;怡情&quot; };
	 * List&lt;String&gt; list = Arrays.asList(arr1);
	 * System.out.println(resourceTool.toStringWith(list)); <br/>Output:<p>张三李四王二麻子Android10086@%~*&^#$hello world怡情</p>
	 * </pre>
	 * 
	 * @param source
	 *            :
	 * @return String
	 */
	public String toStringWith(Collection<String> source) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = source.iterator();
		while (iterator.hasNext()) {
			sb.append(iterator.next());
		}
		return sb.toString();
	}
	public String toStringWith(Collection<String> source,String format) {
		if (format == null) {
			format = "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = source.iterator();
		while (iterator.hasNext()) {
			sb.append(iterator.next()).append(format);
		}
		return sb.toString();
	}
	/**
	 * Input 'String[] ' type parameter,output 'String' data
	 * 
	 * <pre>
	 * String[] arr1 = { &quot;张三&quot;, &quot;李四&quot;, &quot;王二&quot;, &quot;麻子&quot;, &quot;Android&quot;, &quot;10086&quot;, &quot;@%&tilde;*&amp;&circ;#$&quot;,
	 * 		&quot;hello world&quot;, &quot;怡情&quot; };
	 * System.out.println(resourceTool.toStringWith(arr1)); <br/>Output:<p>张三李四王二麻子Android10086@%~*&^#$hello world怡情</p>
	 * </pre>
	 * 
	 * @param source
	 * @return String
	 */
	public String toStringWith(String[] source) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, length = source.length; i < length; i++) {
			sb.append(source[i]);
		}
		return sb.toString();
	}
	public String toStringWith(String[] source,String format) {
		if (format == null) {
			format = "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0, length = source.length; i < length; i++) {
			sb.append(source[i]).append(format);
		}
		return sb.toString();
	}
	/**
	 * Input 'String' type parameter,Formatted output 'String' data
	 * 
	 * <pre>
	 * String str1="今天天气真心不错！ Happy!";
	 * System.out.println(resourceTool.toStringWith(str1,"_"));<br />
	 * Output:今_天_天_气_真_心_不_错_！_H_a_p_p_y_!
	 * </pre>
	 * 
	 * More information,please @see {@code DictTest}
	 * 
	 * @param source
	 * @param format
	 *            : Any value and format also can equals null
	 * @return String
	 */
	public String toStringWith(String source, String format) {
		if (format == null) {
			format = "";
		}
		StringBuilder sb = new StringBuilder();
		String temp = "";
		for (int i = 0, length = source.length(); i < length; i++) {
			temp = source.substring(i, i + 1);
			sb.append(temp.trim());
			if (!temp.matches("\\s*")) {
				if (!(i == length - 1)) {
					sb.append(format);
				}
			}

		}
		source = null;
		return sb.toString();
	}

	/**
	 * Given a hanzi to find pinyin.Eg: '张' ：default return zhang ,besides after
	 * set ResourceType.OutPutStyle But if c="c" that will return "null".Because
	 * 'c' is not hanzi!
	 * 
	 * <pre>
	 * 	Support formatted output style and polyphone:
	 * 			'会'：
	 * 					1.hui 
	 * 					2.kuai
	 * 			'张'：
	 * 					1.zhāng
	 * 	return String[]
	 * 	if it is not a hanzi ,it will return 'null'
	 * </pre>
	 * 
	 * @param c
	 *            : char type
	 * @param styles
	 *            : ResourceType.OutPutStyle type
	 * @return String[]
	 */
	public String[] toPinYinWithStringArray(char c,
			ResourceType.OutPutStyle styles,PinYinStyles pinYinStyles) {
		String cString = String.valueOf(c);
		return toPinYinWithStringArray(cString, styles,PinyinStyle.POLYPHONE,pinYinStyles);
	}
	/**
	 * If argument is a hanzi ,it will return an pinyin ;if not will return 'null' 
	 * </br>
	 * Note:</br>
	 * 	This method not support POLYPHONE,but still support formated output style
	 * @param c
	 * @param styles
	 * @return String
	 */
	public String toPinYinWithString(char c,
			ResourceType.OutPutStyle styles,PinYinStyles pinYinStyles) {
		String cString = String.valueOf(c);
		return toPinYinWithStringArray(cString, styles,PinyinStyle.SINGTON,pinYinStyles)[0];
	}
	/**
	 * {@code toPinYinWithStringArray}
	 * 
	 * @param c
	 * @param styles
	 * @return String[]
	 */
	private String[] toPinYinWithStringArray(String c,
			ResourceType.OutPutStyle styles,PinyinStyle pinyinStyle,PinYinStyles pinYinStyles ) {
		if (styles == null) {
			styles = ResourceType.OutPutStyle.NOTHING;
		}
		if (pinYinJsonObject.containsKey(c)) {
			String[] type = pinYinJsonObject.getString(c).split(",");
			switch (pinyinStyle) {
			case POLYPHONE:
				for (int i = 0, length = type.length; i < length; i++) {
					type[i] = handleType(type[i],styles);
				}
				return type;

			case SINGTON:
				String temp[] = { handleType(type[0],styles) };
				return temp;
			}

		}
		switch (pinYinStyles) {
		case ONLYCHINEASE:
			return new String[] { "null" };
		case COMPLETE:
			return new String[]{c};
		}
		return new String[] { "null" };
	}

	private String[] toPinYinWithStringArray(String c, PinyinStyle pinyinStyle,PinYinStyles pinYinStyles) {
		if (pinYinJsonObject.containsKey(c)) {
			String[] type = pinYinJsonObject.getString(c).split(",");
			switch (pinyinStyle) {
			case POLYPHONE:
				for (int i = 0, length = type.length; i < length; i++) {
					type[i] = handleType(type[i],ResourceType.OutPutStyle.NOTHING);
				}
				return type;

			case SINGTON:
				String temp[] = { handleType(type[0],ResourceType.OutPutStyle.NOTHING) };
				return temp;
			}

		}
		/**
		 * 
		 */
		switch (pinYinStyles) {
		case ONLYCHINEASE:
			return new String[] { "null" };
		case COMPLETE:
			return new String[]{c};
		}
		return new String[] { "null" };
		
	}

	/**
	 * Given a hanzi to find pinyin.Eg: '张' ： return zhang But if c="c" that
	 * will return "null".Because 'c' is not hanzi!
	 * 
	 * <pre>
	 * 	Support polyphone:
	 * 			'会'：
	 * 					1.hui
	 * 					2.kuai
	 * 	return String[]
	 *  if it is not a hanzi please see below code：
	 *  ResourceTool resourceTool = ResourceTool.getInstance();
	 *   resourceTool.toPinYinWithStringArray('a',Utils.PinYinStyles.ONLYCHINEASE)  ==> null 
	 *   resourceTool.toPinYinWithStringArray('a',Utils.PinYinStyles.COMPLETE)  ==> a (itself)
	 *   resourceTool.toPinYinWithStringArray('会',Utils.PinYinStyles.COMPLETE) ==> hui   kuai 
	 *   resourceTool.toPinYinWithStringArray('会',Utils.PinYinStyles.ONLYCHINEASE) ==> hui   kuai 
	 * </pre>
	 * 
	 * {@code toPinYinWithStringArray}
	 * @param c
	 *            : char type
	 * @param pinYinStyles
	 *            : PinYinStyles
	 * @return String[]
	 */
	public String[] toPinYinWithStringArray(char c,PinYinStyles pinYinStyles) {
		String cString = String.valueOf(c);
		return toPinYinWithStringArray(cString,PinyinStyle.POLYPHONE,pinYinStyles);
	}
	/**
	 * The same as {@code toPinYinWithStringArray},except return String type not Array.
	 * If argument is a hanzi ,it will return an pinyin ;if not will return 'null' 
	 * <br />
	 * Note:<br />
	 * 	This method not support POLYPHONE and not support formated output style.<br />
	 * if want to formated output ,please invoke  {@code toPinYinWithString(char c,
			ResourceType.OutPutStyle styles)}
	 * @param c
	 * @return String
	 */
	public String toPinYinWithString(char c,PinYinStyles pinYinStyles) {
		String cString = String.valueOf(c);
		return toPinYinWithStringArray(cString,PinyinStyle.SINGTON,pinYinStyles)[0];
	}
	/**
	 * Invoke this method we can get a key-value
	 * 
	 * <pre>
	 *  	key : Your Hanzi
	 *  	value: PinYin ,Default output style is
	 * {@code ResourceType.OutPutStyle.NOTHING}
	 * 
	 * </pre>
	 * To save  duplicate,So the key is consist of hanzi and a serial number;
	 * More information,please @see {@code DictTest}
	 * @param c
	 * @return Map
	 */
	public Map<String, String[]> toPinYinWithMap(String c) {
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
		if (c.equals("") || c == null) {
			map.put(null, new String[] { "null" });
			return map;
		}
		String temp = c.trim();
		for (int j = 0, length1 = temp.length(); j < length1; j++) {
			String cc = temp.substring(j, j + 1);
			if (pinYinJsonObject.containsKey(cc)) {
				String[] type = pinYinJsonObject.getString(cc).split(",");
				for (int i = 0, length = type.length; i < length; i++) {
					type[i] = handleType(type[i],
							ResourceType.OutPutStyle.NOTHING);
				}
				map.put(String.valueOf(j) + cc, type);
			}
		}
		return map;
	}

	public Map<String, String[]> toPinYinWithMapMixModul(String c) {
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
		if (c.equals("") || c == null) {
			map.put(null, new String[] { "null" });
			return map;
		}
		String temp = c.trim();
		for (int j = 0, length1 = temp.length(); j < length1; j++) {
			String cc = temp.substring(j, j + 1);
			if (pinYinJsonObject.containsKey(cc)) {
				String[] type = pinYinJsonObject.getString(cc).split(",");
				for (int i = 0, length = type.length; i < length; i++) {
					type[i] = handleType(type[i],
							ResourceType.OutPutStyle.NOTHING);
				}
				map.put(String.valueOf(j) + cc, type);
			} else {
				map.put(String.valueOf(j) + cc, new String[] { cc });
			}
		}
		return map;
	}

	/**
	 * Invoke this method we can get a key-value
	 * 
	 * <pre>
	 *  	key : Your Hanzi
	 *  	value: PinYin , Output style is
	 * {@code ResourceType.OutPutStyle}
	 * 
	 * <pre>
	 * To save  duplicate,So the key is consist of hanzi and a serial number;
	 * More information,please @see {@code DictTest}
	 * 
	 * @param c
	 * @param style
	 * @return A map
	 */
	public Map<String, String[]> toPinYinWithMap(String c,
			ResourceType.OutPutStyle style) {
		if (style == null) {
			style = ResourceType.OutPutStyle.NOTHING;
		}
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
		if (c.equals("") || c == null) {
			map.put(null, new String[] { "null" });
			return map;
		}
		String temp = c.trim();
		// if(utils.hasChinese(temp)){
		// temp = utils.onlyChinese(temp);
		// }
		for (int j = 0, length1 = temp.length(); j < length1; j++) {
			String cc = temp.substring(j, j + 1);
			if (pinYinJsonObject.containsKey(cc)) {
				String[] type = pinYinJsonObject.getString(cc).split(",");
				for (int i = 0, length = type.length; i < length; i++) {
					type[i] = handleType(type[i], style);
				}
				map.put(String.valueOf(j) + cc, type);
			}
		}
		return map;
	}

	/**
	 * The same as {@code toPinYinWithMap} except if invoke
	 * {@code toPinYinWithMapMixModul} it does not filter hanzi Eg:
	 * 
	 * <pre>
	 * 		String c = "hello world 张",Not only output 'zhang',in fact it will output:hello world zhang
	 * </pre>
	 * 
	 * @param c
	 * @param style
	 * @return Map
	 */
	public Map<String, String[]> toPinYinWithMapMixModul(String c,
			ResourceType.OutPutStyle style) {
		if (style == null) {
			style = ResourceType.OutPutStyle.NOTHING;
		}
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
		if (c.equals("") || c == null) {
			map.put(null, new String[] { "null" });
			return map;
		}
		String temp = c.trim();
		// if(utils.hasChinese(temp)){
		// temp = utils.onlyChinese(temp);
		// }
		for (int j = 0, length1 = temp.length(); j < length1; j++) {
			String cc = temp.substring(j, j + 1);
			if (pinYinJsonObject.containsKey(cc)) {
				String[] type = pinYinJsonObject.getString(cc).split(",");
				for (int i = 0, length = type.length; i < length; i++) {
					type[i] = handleType(type[i], style);
				}
				map.put(String.valueOf(j) + cc, type);
			} else {
				map.put(String.valueOf(j) + cc, new String[] { cc });
			}
		}
		return map;
	}

	/**
	 * A help method for {@code findSimplyPinYinWithHanZi}
	 * 
	 * @param code
	 * @param styles
	 * @return String
	 */
	@SuppressWarnings("incomplete-switch")
	public String handleType(String code, ResourceType.OutPutStyle styles) {
		StringBuilder sb = new StringBuilder();
		String tone = null;
		String nothing = "";
		if (styles == null) {
			styles = ResourceType.OutPutStyle.NOTHING;
		}
		for (int i = 0, length = code.length(); i < length; i++) {
			String c = code.substring(i, i + 1);
			switch (styles) {
			case SHENGMU:
				int num = 0;
				for (int j = 0, length1 = SHENGMU.length; j < length1; j++) {
					num = code.indexOf(SHENGMU[j]);
					if (num == 0) {
						sb.append(SHENGMU[j]);
					}
				}
				return sb.toString();
			case YUNMU:
				int num1 = 0;
				for (int j = 0, length1 = SHENGMU.length; j < length1; j++) {
					num1 = code.indexOf(SHENGMU[j]);
					if (!(num1 == 0)) {
						sb.append(code.substring(SHENGMU[j].length()));
						break;
					}
				}
				return sb.toString();
			}
			if (soundMarkJsonObject.containsKey(c)) {
				// System.out.println("code[i]="+i+ " "+ c);
				tone = (String) soundMarkJsonObject.get(c);
				// System.out.println("tone="+tone);
				nothing = tone.replaceAll("[0-9]", "");
				switch (styles) {
				/* Without Tone,Without Number */
				case WITHTONE:
					sb.append(c);
					break;
				/* With Number */
				case NUMBER:
					sb.append(tone);
					break;
				/* With Tone */
				case NOTHING:
					sb.append(nothing);
					break;
				default:
					sb.append(tone.replaceAll("[0-9]", ""));
					break;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
