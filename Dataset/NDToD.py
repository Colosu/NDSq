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
		file = FileInput(path+"/"+filename, inplace=1)
		i = 0
		for line in file:
			if "->" in line:
				lineSplit = line.split(" / ")
				lineW = lineSplit[0] + "&" + str(i) + "&" + " / " + lineSplit[1]
				line = lineW
				i = i + 1
			print(line, end='')
		file.close()