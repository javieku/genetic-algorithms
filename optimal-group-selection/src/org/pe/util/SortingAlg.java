package org.pe.util;

public class SortingAlg {
	
	public static void quicksort(Integer[] buffer,int low, int n){
		  int lo = low;
		  int hi = n;
		  if (lo >= n) {
			  return;
		  }
		  int mid = buffer[(lo + hi) / 2];
		  while (lo < hi) {
			  while (lo<hi && buffer[lo] < mid) {
				  lo++;
			  }
			  while (lo<hi && buffer[hi] > mid) {
				  hi--;
			  }
			  if (lo < hi) {
				  int T = buffer[lo];
				  buffer[lo] = buffer[hi];
				  buffer[hi] = T;
			  }
		  }
		  if (hi < lo) {
			  int T = hi;
			  hi = lo;
			  lo = T;
		  }
		  quicksort(buffer, low, lo);
		  quicksort(buffer, lo == low ? lo+1 : lo, n);
	}

}
