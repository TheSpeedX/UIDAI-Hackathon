import xml.etree.ElementTree as ET


def parse_xml(data):
    """
    Parse xml data
    """
    user_data = {}
    fields = ["name", "dob", "gender"]
    try:
        tree = ET.fromstring(data)
        for field in fields:
            user_data[field] = tree.find('UidData/Poi').attrib[field]
    except Exception as e:
        print(e)
    return user_data

