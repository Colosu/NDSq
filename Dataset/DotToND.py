#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan 10 15:14:34 2020

@author: colosu
"""

# Pythono3 code to transform .kiss2 files to .dot files
  
# importing os module 
import os 
from fileinput import FileInput

path = "Dot"
repeat = True

for filename in os.listdir(path):
	if filename[-4:] == ".dot":
		repeat = True
		while repeat:	
			repeat = False
			file = FileInput(path+"/"+filename, inplace=1)
			for line in file:
				if "-" in line[line.find('>'):]:
					pos = line.find('-',line.find('>'))
					lineW1 = line[:pos] + "0" + line[pos+1:]
					lineW2 = line[:pos] + "1" + line[pos+1:]
					line = lineW1 + lineW2
					repeat = True
				print(line, end='')
			file.close()