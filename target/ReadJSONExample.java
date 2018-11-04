package com.howtodoinjava.demo.jsonsimple;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sun.invoke.empty.Empty;
import sun.reflect.generics.tree.Tree;



public class ReadJSONExample 
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) 
	{
		//JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		
		try (FileReader reader = new FileReader("test.json"))
		{
			//Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;
            System.out.println(employeeList);
            
            //Iterate over employee array
            employeeList.forEach( emp -> {
            	JSONObject employeeObject = (JSONObject) ((JSONObject) emp).get("process");
            	String parent = (String) employeeObject.get("parent");
            	if (parent.compareTo("-1")==0)
            		{
            			TreeNode treeRoot = new TreeNode((String) employeeObject.get("value"),null);
            	        Queue<TreeNode> myTree = new LinkedList<>();
            	        myTree.add(treeRoot);
            	        makeTree(myTree,treeRoot,employeeList);
            		}
            }  );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}

	@SuppressWarnings("unchecked")
private static void makeTree(Queue<TreeNode> tree, TreeNode root, JSONArray list) 
	{
		while (!tree.isEmpty())
		{
			TreeNode temp = tree.remove();
			String value = temp.getValue();
			list.forEach(emp -> {
				JSONObject employeeObject = (JSONObject) ((JSONObject) emp).get("process");
				String empParent = (String) employeeObject.get("parent");
				if (empParent.compareTo(value)==0)
				{
					TreeNode newNode = new TreeNode((String) employeeObject.get("value"),temp);
					temp.addChild(newNode);
					tree.add(newNode);
					System.out.println(newNode.getValue());
					System.out.println(newNode.getParent().getValue());
					System.out.println(newNode.getChildren());
				}
			});
		}
		System.out.println(root.getValue());
		System.out.println(root.getParent());
		Iterator<TreeNode> iterat = root.iterator();
		System.out.println(iterat.next().getValue());
		System.out.println(iterat.hasNext());
		System.out.println(iterat.next().getValue());
		System.out.println(iterat.hasNext());
	}
}
