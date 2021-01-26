/**
 * 
 */
package myCB.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import myCB.CGS.Molecule;

/**
 * @author Bro
 *
 */
public class Serial<template> implements Comparable<Serial<template>>, Serializable{
	protected LinkedList<template> data;
	private transient ListIterator<template> ptr;
	protected Double value;
	private String debug;
	private static final long serialVersionUID = 1L;
	/*---------------
	 * Constructors  
	*/
	public Serial(){
		data = new LinkedList<template>();
		value = 0.0;
		}
	
	public Serial(LinkedList<template> in){
		data = new LinkedList<template>();
		data.addAll(in);
		value = 0.0;
		}
	
	public Serial(Serial<template> in){
		data = new LinkedList<template>();
		data.addAll(in.data);
		value = 0.0;
		}
	
	public Serial(template in){
		data = new LinkedList<template>();
		data.add(in);
		value = 0.0;
		}
	/*---------------
	 * These functions control what is in the datatype 
	*/
	public void add(template in){
		data.add(in);
		}
	
	public void add(LinkedList<template> in){
		data.addAll(in);
		}
	
	public void add(Serial<template> in){
		data.addAll(in.data);
		}
	
	public void add_front(template in){
		data.addFirst(in);
		}
	
	public template remove(){
		return data.removeLast();
		}
	
	public template remove(int i){
		return data.remove(i);
		}
	
	public template remove_front(){
		return data.removeFirst();
		}
	
	public template get(int i){
		return data.get(i);
		}
	
	public template get_front(){
		return data.getFirst();
		}
	
	public template get_back(){
		return data.getLast();
		}
	/*---------------
	 * These functions control transitioning through the datatype 
	*/
	public ListIterator<template> listIterator(){
		return data.listIterator();
		}
	
	public void start(){
		ptr = data.listIterator();
		}
	
	public void back(){
		ptr = data.listIterator(data.size());
		}
	
	public Boolean hasNext(){
		return ptr.hasNext();
		}
	
	public Boolean hasPrev(){
		return ptr.hasPrevious();
		}
	
	public template next(){
		return ptr.next();
		}
	
	public template prev(){
		return ptr.previous();
		}
	/*---------------
	 * These functions are additional to facilitate data
	*/
	public int size(){
		return data.size();
		}
	
	public int at(){
		if (ptr == null)
			return 0;
		else
			return ptr.nextIndex()-1;
		}
	
	public LinkedList<template> segment(int i, int j){
		return new LinkedList<template>(data.subList(i,j));
		}
	
	public void setValue(Double in){
		value = in;
		}
	
	public Double getValue(){
		return value;
		}

	public int compareTo(Serial<template> that) {
		return (int)((that.getValue() - this.getValue())*10000);
		}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		Serial<template> that;
		
		try	{
			that = (Serial<template>)o;
			}
		catch (ClassCastException ex)
			{
			return false;
			}
		
		if (that.data.size() != this.data.size())
			return false;
		
		return that.data.equals(this.data);
		}
	
	public void setDebug(String in){
		debug = in;
		}
	
	public String toString(){
		String temp = "[";
		
		for (start(); hasNext(); temp += next().toString()+", "){}
		
		temp += getValue()+"]\n";
		
		if (debug != null)
			temp += "("+debug+")\n";
		return temp;
		}
	
	public int hashCode(){
		return data.hashCode();
		}
	
	public void clean(){
		HashSet<template> hash = new HashSet<template>();
		ListIterator<template> i = data.listIterator();
		
		while (i.hasNext())
			hash.add(i.next());
		
		data = new LinkedList<template>(hash);
		}
	}