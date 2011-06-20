package gameserver.skillengine.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author kecimis
 */

@XmlType(name = "TransformType")
@XmlEnum
public enum TransformType {
    AVATAR,
    PC,
    NONE
}