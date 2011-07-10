package com.aionemu.packetsamurai.utils;

import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.SortedMap;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;

public class NpcWalkExporter {
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdMap = new TreeMap<String, String>();
	private FastMap<String, FastList<Move>> moves = new FastMap<String, FastList<Move>>();

	public NpcWalkExporter(List<DataPacket> packets, String sessionName) {
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse() {
		String fileName = "npc_walker_" + sessionName + ".xml";

		try {
			//Get NpcIDs
			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					String objId = "";
					String npcId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npcId = valuePart.readValue();
						}
						else if("objId".equals(partName))
						{
							objId = valuePart.readValue();
						}
					}
					if(!"0".equals(objId))
					{
						npcIdMap.put(objId, npcId);
					}
				}
			}


			for (DataPacket packet : packets) {
				String packetName = packet.getName();

				if ("SM_MOVE".equals(packetName)) {
					Move movement = new Move();
					String objectId = "";
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName))
							movement.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							movement.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							movement.z = Float.parseFloat(valuePart.readValue());
						else if ("objId".equals(partName))
							objectId = valuePart.readValue();
					}
					if(!moves.containsKey(objectId) && !objectId.equals(""))
						moves.put(objectId, new FastList<Move>());
					moves.get(objectId).add(movement);
				}
			}


			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

			for (FastMap.Entry<String, FastList<Move>> e = moves.head(), end = moves.tail(); (e = e.getNext()) != end;)
			{
				String npcId = "";
				String npcObjId = e.getKey();

				for(Entry<String, String> entry : npcIdMap.entrySet())
				{
					if(entry.getKey().equals(npcObjId))
						npcId = entry.getValue();
				}

				if(!npcId.equals(""))
				{
					StringBuilder sb = new StringBuilder();

					sb.append("	<walker_template route_id=\"" + e.getKey() + "\">\n");
					sb.append("		<routes>\n");
					sb.append("		<!-- NpcID: " + npcId + " -->\n");

					for (int i = 1; i <= e.getValue().size(); i++)
					{
						Move lsMove = e.getValue().get(i - 1);
						sb.append("			<routestep step=\"" + i + "\" loc_x=\"" + lsMove.x + "\" loc_y=\"" + lsMove.y + "\" loc_z=\"" + lsMove.z + "\" rest_time=\"0\"/>\n");
					}

					sb.append("		</routes>\n");
					sb.append("	</walker_template>\n");

					out.write(sb.toString());
				}

			}

			out.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log("NPC Walker Has Been Written Successful");
	}

	class Move {
		float x;
		float y;
		float z;
	}
}