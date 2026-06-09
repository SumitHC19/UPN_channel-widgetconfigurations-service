package com.aonhewitt.upoint.util;

import java.util.ArrayList;
import java.util.List;

import com.aonhewitt.upoint.db.JNDIClientMapping;

public class MockJNDIRepository {
	String getDBServerName(String name) {
		return "jdbc:oracle:thin:@localhost:1521:xe";
	}

	List<JNDIClientMapping> getJNDIClientMappingList() {
		JNDIClientMapping mapping = new JNDIClientMapping();
		mapping.setJndiName("client");
		mapping.setDbUrl("jdbc:oracle:thin:@localhost:1521:xe");

		List<JNDIClientMapping> list = new ArrayList<JNDIClientMapping>();
		list.add(mapping);

		return list;
	}
}


	
