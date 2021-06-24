#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
@author: colosu
"""

file = open("test.txt", "w")

vals = {1,2,5}
magnitudD = {100,1000,10000,100000,1000000,10000000}
magnitudIO = {10, 100}

for m in sorted(magnitudD):
	for v in sorted(vals):
		for O in sorted(magnitudIO):
			for vO in sorted(vals):
				for I in sorted(magnitudIO):
					for vI in sorted(vals):
						if O*vO < m*v and I*vI <= O*vO:
							file.write(f'{m*v} {O*vO} {I*vI}\r\n')
						