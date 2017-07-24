package com.zfysoft.platform.util;

import java.io.IOException;
import java.util.Date;

public class ValidateCodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ValidateCode vCode = new ValidateCode(120,40,4,20);
		try {
			String path="D:/1.png";
			System.out.println(vCode.getCode()+" >"+path);
			vCode.write(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
