package org.pe.util;

import java.util.Arrays;

public class PermUtil <T> {
	 private T[] arr;
	 private int[] permSwappings;
	 
	 private int nperm;
	 private int counter;

	 public PermUtil(T[] arr) {
		this(arr,arr.length);
		nperm = UsefulMethods.factorial(arr.length);
	 }

	 public PermUtil(T[] arr, int permSize) {
		  this.arr = arr.clone();
		  this.permSwappings = new int[permSize];
		  for(int i = 0;i < permSwappings.length;i++)
			  permSwappings[i] = i;
	 }

	 public T[] next() {
	  if (arr == null)
	   return null;

	  T[] res = Arrays.copyOf(arr, permSwappings.length);

	  int i = permSwappings.length-1;
	  while (i >= 0 && permSwappings[i] == arr.length - 1) {
		   swap(i, permSwappings[i]);
		   permSwappings[i] = i;
		   i--;
	  }

	  if (i < 0)
		  arr = null;
	  else {   
		   int prev = permSwappings[i];
		   swap(i, prev);
		   int next = prev + 1;
		   permSwappings[i] = next;
		   swap(i, next);
	  }

	  counter++;
	  
	  return res;
	 }
	 
	 public T[] nextwoRep() {
		boolean found = false;
		T[] res = null;
		while(!found && counter < nperm){
			res = next();
			found = !repeated(res);
		}
		return res;
	 }
	

	private boolean repeated(T[] arr) {
		boolean found = false;
		int i = 0;
		int j = 0;
		while(!found && i < arr.length){
			j = i + 1;
			while(!found && j < arr.length){
				found = arr[i].equals(arr[j]);
				j++;
			}
			i++;
		}
		return found;
	}

	private void swap(int i, int j) {
		  T tmp = arr[i];
		  arr[i] = arr[j];
		  arr[j] = tmp;
	 }

}