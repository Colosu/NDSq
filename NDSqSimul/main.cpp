/*
 * main.cpp
 *
 *  Created on: 8 sept. 2017
 *      Author: colosu
 */

#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>
#include <cstdlib>
#include <ctime>
#include <cmath>
#include <map>
#include <vector>
#include <algorithm>
#include <gsl/gsl_statistics.h>
using namespace std;

#define ITERS 10
#define REP 200

/*
double PColl(unsigned int d, unsigned int div[]);
double Sq(unsigned int d, unsigned int div[]);
double PSq(unsigned int d, unsigned int div[], double s);
double DRR(unsigned int d, unsigned int div[]);
double Pearson(double pc[100], double ps[100]);
double Spearman(double pc[100], double ps[100]);
unsigned int* setSubdivision(unsigned int d, unsigned int m);
*/

int main() {

	srand(time(NULL));
	string Ifile = "test.txt";
	string Ofile = "PearsonResults.txt";
	string Ofile2 = "SpearmanResults.txt";

	ifstream IFile;
	ofstream OFile;
	ofstream OFile2;

	IFile.open(Ifile);
	if (!IFile.is_open()) {
		cerr << "I can't open the input file." << endl;
		return 1;
	}
	OFile.open(Ofile);
	if (!OFile.is_open()) {
		cerr << "I can't create the output file." << endl;
		return 1;
	}
	OFile2.open(Ofile2);
	if (!OFile2.is_open()) {
		cerr << "I can't create the output file." << endl;
		return 1;
	}
	unsigned int dI = 0;
	unsigned int dO = 0;
	unsigned int mO = 0;
	unsigned int mI = 0;
	double pc;
	double pd;
	double Os;
//	double Ops;
//	double Is;
	double Ips;
	double IOs;
	double IOps;
	double pcd[REP];
	double ps[REP];
	double s[REP];
	double Pps = 0;
	double Ps = 0;
	double Sps = 0;
	double Ss = 0;
	unsigned int sum = 0;
	double aux[2*REP];

	OFile << "| Lenght | Maximun O size | Maximun I size | PSq | Sq |" << endl;
	OFile2 << "| Lenght | Maximun O size | Maximun I size | PSq | Sq |" << endl;
	while (IFile.peek() != EOF) {
		IFile >> dI >> mO >> mI;
		Pps = 0;
		Ps = 0;
		Sps = 0;
		Ss = 0;
		for (int I = 0; I < ITERS; I++) {
			for (int i = 0; i < REP; i++) {
				sum = 0;
				unsigned int* divO = new unsigned int[dI];
				vector<unsigned int>** vO = new vector<unsigned int>*[dI];
				unsigned int* divI = new unsigned int[dI+1];
				vector<unsigned int>** vI = new vector<unsigned int>*[dI+1];
				vector<pair<unsigned int, unsigned int>> outToIn;

				unsigned int n = 0;
				unsigned int j = 0;
				while (sum < dI && j < dI) {
					n = rand() % mO;
					while (n <= 0) {
						n = rand() % mO;
					}
					sum += n;
					divO[j] = n;
					vO[j] = new vector<unsigned int>;
					outToIn.push_back({j,n});
					j++;
				}

				if (sum > dI) {
					divO[j-1] = divO[j-1] - (sum - dI);
					outToIn[j-1] = {j-1,divO[j-1]};
				}

				divO[j] = 0;
				dO = j;

				sum = 0;
				n = 0;
				unsigned int k = 0;
				unsigned int x = 0;
				while (sum < dO && k < dI) {
					n = rand() % min(mI,dO);
					while (n <= 0 || sum + n > dO) {
						n = rand() % min(mI,dO);
					}
					sum += n;
					divI[k] = n;
					vI[k] = new vector<unsigned int>;
					for (unsigned int z = 0; z < n; z++) {
						if (!outToIn.empty()) {
							x = rand() % outToIn.size();
							while (find(vI[k]->begin(), vI[k]->end(), outToIn[x].first) != vI[k]->end()) {
								x = rand() % outToIn.size();
							}
							vI[k]->push_back(outToIn[x].first);
							vO[outToIn[x].first]->push_back(k);
							outToIn[x] = {outToIn[x].first, outToIn[x].second-1};
							if (outToIn[x].second <= 0) {
								outToIn.erase(outToIn.begin()+x);
							}
							if (outToIn.empty()) {
								divO[dO+1] = 0;
								vO[dO] = new vector<unsigned int>;
								dO++;
							}
						} else {
							vI[k]->push_back(dO-1);
							vO[dO-1]->push_back(k);
							divO[dO-1]++;
						}
					}
					k++;
				}

				while (k < dI) {
					divI[k] = 1;
					vI[k] = new vector<unsigned int>;
					if (outToIn.empty()) {
						vI[k]->push_back(dO-1);
						vO[dO-1]->push_back(k);
						divO[dO-1]++;
						if (divO[dO] == mO) {
							divO[dO+1] = 0;
							vO[dO] = new vector<unsigned int>;
							dO++;
						}
					} else {
						x = rand() % outToIn.size();
						vI[k]->push_back(outToIn[x].first);
						vO[outToIn[x].first]->push_back(k);
						outToIn[x] = {outToIn[x].first, outToIn[x].second-1};
						if (outToIn[x].second <= 0) {
							outToIn.erase(outToIn.begin()+x);
						}
						if (outToIn.empty()) {
							divO[dO+1] = 0;
							vO[dO] = new vector<unsigned int>;
							dO++;
						}
					}
					k++;
				}

				divI[k] = 0;

				pc = 0;
				pd = 0;
				pcd[i] = 0;
				Os = 0;
//				Ops = 0;
//				Is = 0;
				Ips = 0;
				IOs = 0;
				IOps = 0;
				s[i] = 0;
				ps[i] = 0;
				int l = 0;
//				double inverse = 0;
//				double prob = 0;
				while (divO[l] != 0) {
					pc += ((double)divO[l]*(divO[l]-1))/(dI*(dI-1));
//					prob = 0;
//					for (unsigned int z = 0; z < vO[l]->size(); z++) {
//						prob += 1.0/divI[vO[l]->at(z)];
//					}
//					Os += (double)prob/dI * log2(prob);
					Os += (double)divO[l]/dI * log2(divO[l]);
//					inverse = 0;
//					for (unsigned int z = 0; z < vO[l]->size(); z++) {
////						prob = 0;
////						for (unsigned int Z = 0; Z < vI[vO[l]->at(z)]->size(); Z++) {
////							prob += 1.0/divO[vI[vO[l]->at(z)]->at(Z)];
////						}
////						inverse += prob;
//						inverse += divI[vO[l]->at(z)];
//					}
//					for (unsigned int z = 0; z < vO[l]->size(); z++) {
////						prob = 0;
////						for (unsigned int Z = 0; Z < vI[vO[l]->at(z)]->size(); Z++) {
////							prob += 1.0/divO[vI[vO[l]->at(z)]->at(Z)];
////						}
////						Ops += ((double)prob/inverse) * log2(((double)prob/inverse));
//						Ops += ((double)divI[vO[l]->at(z)]/inverse) * log2(((double)divI[vO[l]->at(z)]/inverse));
//					}
					l++;
				}
				l = 0;
				while (divI[l] != 0) {
					pd += ((double)divI[l]*(divI[l]-1))/(dO*(dO-1));
//					ps[i] += (double)size(divI[l])/dO * log2(size(divI[l]));
//					prob = 0;
//					for (unsigned int z = 0; z < vI[l]->size(); z++) {
//						prob += 1.0/divO[vI[l]->at(z)];
//					}
//					Ips += (double)prob/dO * log2(prob);
					Ips += (double)divI[l]/dO * log2(divI[l]);
//					inverse = 0;
//					for (unsigned int z = 0; z < vI[l]->size(); z++) {
////						prob = 0;
////						for (unsigned int Z = 0; Z < vO[vI[l]->at(z)]->size(); Z++) {
////							prob += 1.0/divI[vO[vI[l]->at(z)]->at(Z)];
////						}
////						inverse += prob;
//						inverse += divO[vI[l]->at(z)];
//					}
//					for (unsigned int z = 0; z < vI[l]->size(); z++) {
////						prob = 0;
////						for (unsigned int Z = 0; Z < vO[vI[l]->at(z)]->size(); Z++) {
////							prob += 1.0/divI[vO[vI[l]->at(z)]->at(Z)];
////						}
////						Is += ((double)prob/inverse) * log2(((double)prob/inverse));
//						Is += ((double)divO[vI[l]->at(z)]/inverse) * log2(((double)divO[vI[l]->at(z)]/inverse));
//					}
					l++;
				}

				pcd[i] = pc + pd;
				s[i] = Os;
				IOs = Os;// + Is/dI;
				IOps = Ips;// + Ops/dO;
				ps[i] = IOs + IOps;
				delete divO;
				for (unsigned int c = 0; c < dO; c++) {
					delete vO[c];
				}
				delete vO;
				delete divI;
				for (unsigned int c = 0; c < dI; c++) {
					delete vI[c];
				}
				delete vI;
			}
			Pps += gsl_stats_correlation(pcd, 1, ps, 1, REP);
			Ps += gsl_stats_correlation(pcd, 1, s, 1, REP);
			Sps += gsl_stats_spearman(pcd, 1, ps, 1, REP, aux);
			Ss += gsl_stats_spearman(pcd, 1, s, 1, REP, aux);
//			OFile << dI << " & " << mO << " & " << mI << " & " << Pps << " & " << Ps << "\\\\" << endl;
//			OFile << "\\hline" << endl;
//			OFile2 << dI << " & " << mO << " & " << mI << " & " << Sps << " & " << Ss << "\\\\" << endl;
//			OFile2 << "\\hline" << endl;
		}
		OFile << dI << " & " << mO << " & " << mI << " & " << Pps/(double)ITERS << " & " << Ps/(double)ITERS << "\\\\" << endl;
		OFile << "\\hline" << endl;
		OFile2 << dI << " & " << mO << " & " << mI << " & " << Sps/(double)ITERS << " & " << Ss/(double)ITERS << "\\\\" << endl;
		OFile2 << "\\hline" << endl;
		if(IFile.peek() == '\n') {
			IFile.ignore(1);
		}
	}
	IFile.close();
	OFile.close();
	OFile2.close();
	return 0;
}

/*
double PColl(unsigned int d, unsigned int div[]) {
	int i = 0;
	double sum = 0;
	while (div[i] != 0) {
		sum += ((double)div[i]*(div[i]-1))/(d*(d-1));
		i++;
	}
	return sum;
}

double Sq(unsigned int d, unsigned int div[]) {
	int i = 0;
	double sum = 0;
	while (div[i] != 0) {
		sum += pow((double)div[i]/d, 2) * log2(d);
		i++;
	}
	return sum;
}

double PSq(unsigned int d, unsigned int div[], double s) {
	int i = 0;
	unsigned int max = 0;
	while (div[i] != 0) {
		if (div[i] > max) {
			max = div[i];
		}
		i++;
	}
	return s/log2(max);
}

double DRR(unsigned int d, unsigned int div[]) {
	int i = 0;
	unsigned int sum = 0;
	while (div[i] != 0) {
		sum++;
		i++;
	}
	return (double)d/sum;
}

double Pearson(double pc[100], double ps[100]) {

	return gsl_stats_correlation(pc, 1, ps, 1, REP);
}

double Spearman(double pc[100], double ps[100]) {

	double aux[2*REP];
	return gsl_stats_spearman(pc, 1, ps, 1, REP, aux);
}

unsigned int* setSubdivision(unsigned int d, unsigned int m) {
	unsigned int sum = 0;
	unsigned int* div = new unsigned int[d];
	for (unsigned int j = 0; j < d; j++) {
		div[j] = 0;
	}
	unsigned int n = 0;
	unsigned int j = 0;
	while (sum < d && j < d) {
		n = rand() % m;
		while (n <= 0) {
			n = rand() % m;
		}
		sum += n;
		div[j] = n;
		j++;
	}

	if (sum > d) {
		div[j-1] = div[j-1] - (sum - d);
	}

	return div;
}
*/
