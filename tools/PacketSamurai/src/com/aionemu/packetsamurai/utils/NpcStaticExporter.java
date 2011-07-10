package com.aionemu.packetsamurai.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;

/**
 * @author ATracer - Kamui
 */
public class NpcStaticExporter
{
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdTitleMap = new TreeMap<String, String>();

	public NpcStaticExporter(List<DataPacket> packets, String sessionName) 
	{
		super();
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse()
	{
		String fileName = "npc_static_id_" + sessionName + ".xml";

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					String npcId = "";
					String static_id = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npcId = valuePart.readValue();
						}
						else if("static_id".equals(partName))
						{
							static_id = valuePart.readValue();
						}
					}
					if(!"0".equals(static_id))
					{
						npcIdTitleMap.put(npcId, static_id);
					}
				}
			}

			for(Entry<String, String> entry : npcIdTitleMap.entrySet())
			{
				StringBuilder sb = new StringBuilder();
				sb.append("<info npcid=\"");
				sb.append(entry.getKey());
				sb.append("\" staticid=\"");
				sb.append(entry.getValue());
				sb.append("\" />\n");
				out.write(sb.toString());
			}
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log("NPC Static IDs Have Been Written Successful");
	}
}
