#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan 10 15:14:34 2020

@author: colosu
"""

# Pythono3 code to transform .kiss2 files to .dot files
  
# importing os module 
import os 

pathO = "Kiss2"
pathD = "Dot"

for filename in os.listdir(pathO):
	if filename[-6:] == ".kiss2":
		fileO = open(pathO+"/"+filename, 'r')
		fileD = open(pathD+"/"+filename[:-6]+".dot", 'w')
		fileD.write("digraph g {\n")
		line = fileO.readline()
		while ".s" not in line:
			line = fileO.readline()
		split = line.split(" ")
		for i in range(int(split[1])):
			if i == 0:
				fileD.write(str(i)+" [color=\"red\"]\n")
			else:
				fileD.write(str(i) + "\n")
		for line in fileO:
			split = line.split(" ")
			if ("state" in split[1]):
				split[1] = split[1][5:]
			if ("state" in split[2]):
				split[2] = split[2][5:]
			if ("bit" in split[1]):
				split[1] = split[1][3:]
			if ("bit" in split[2]):
				split[2] = split[2][3:]
			if ("st" in split[1]):
				split[1] = split[1][2:]
			if ("st" in split[2]):
				split[2] = split[2][2:]
			if ("t" in split[1]):
				split[1] = split[1][1:]
			if ("t" in split[2]):
				split[2] = split[2][1:]
			if ("*" == split[1]):
				split[1] = "0"
			if ("*" == split[2]):
				split[2] = "0"
			if ("A" == split[1]):
				split[1] = "10"
			if ("A" == split[2]):
				split[2] = "10"
			if ("B" == split[1]):
				split[1] = "11"
			if ("B" == split[2]):
				split[2] = "11"
			if ("C" == split[1]):
				split[1] = "12"
			if ("C" == split[2]):
				split[2] = "12"
			if ("D" == split[1]):
				split[1] = "13"
			if ("D" == split[2]):
				split[2] = "13"
			if ("E" == split[1]):
				split[1] = "14"
			if ("E" == split[2]):
				split[2] = "14"
			if ("F" == split[1]):
				split[1] = "15"
			if ("F" == split[2]):
				split[2] = "15"
			fileD.write(split[1] + " -> " + split[2] + " [label=\"" + split[0] + " / " + split[3][:-1] + "\"]\n")
		fileD.write("}")
		fileD.flush()
			