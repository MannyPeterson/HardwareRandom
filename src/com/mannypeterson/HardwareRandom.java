/*
*
* True Random Number Generator (TRNG)
* Copyright (C) 2020 Manny Peterson <me@mannypeterson.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/
package com.mannypeterson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HardwareRandom {
	private int cursor;
	private int[] pool;

	public HardwareRandom() {
		this.cursor = -1;
		this.pool = new int[128];
	}

	private int hexToDec(char a, char b) {
		final char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int r = 0;
		for (int i = 0; i < 16; i++) {
			if (a == c[i]) {
				r = 16 * r + i;
				break;
			}
		}
		for (int i = 0; i < 16; i++) {
			if (b == c[i]) {
				r = 16 * r + i;
				break;
			}
		}
		return r;
	}

	private void load() {
		String chunk = null;
		try {
			URL url = new URL("http://mannypeterson.com/trng/get.php?request=1");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
			chunk = bufferedReader.readLine();
			bufferedReader.close();
			if (chunk.length() == 256) {
				for (int i = 0; i < 128; i++) {
					this.pool[i] = hexToDec(chunk.charAt(2 * i), chunk.charAt(2 * i + 1));
				}
				this.cursor = 127;
			}
		} catch (IOException e) {

		}
	}

	public int nextInt() {
		if (this.cursor < 0) {
			this.load();
		}
		return this.pool[this.cursor--];
	}
}
