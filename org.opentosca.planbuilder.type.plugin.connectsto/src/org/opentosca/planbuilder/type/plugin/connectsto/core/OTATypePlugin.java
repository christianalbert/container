package org.opentosca.planbuilder.type.plugin.connectsto.core;

import javax.xml.soap.Node;

import org.opentosca.planbuilder.model.tosca.AbstractNodeTemplate;
import org.opentosca.planbuilder.model.tosca.AbstractRelationshipTemplate;
import org.opentosca.planbuilder.model.utils.ModelUtils;
import org.opentosca.planbuilder.plugins.IPlanBuilderTypePlugin;
import org.opentosca.planbuilder.plugins.context.PlanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class OTATypePlugin<T extends PlanContext> implements IPlanBuilderTypePlugin<T> {

    private static final String PLUGIN_ID = "OpenTOSCA PlanBuilder Type Plugin IoT";
    private static final Logger LOG = LoggerFactory.getLogger(OTATypePlugin.class);

    @Override
    public boolean canHandle(final AbstractNodeTemplate nodeTemplate) {
        if (nodeTemplate.getProperties() == null) {
            return false;
        }

        // Check if is a OTA Manager
        if (!ModelUtils.getNodeTypeHierarchy(nodeTemplate.getType())
                       .contains(IoTTypePluginConstants.OTA_Manager_NODETYPE)) {
            return false;
        }

        // Check if it has host, user and password
        final Element propertyElement = nodeTemplate.getProperties().getDOMElement();
        final NodeList childNodeList = propertyElement.getChildNodes();

        int check = 0;
        for (int index = 0; index < childNodeList.getLength(); index++) {
            if (childNodeList.item(index).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (childNodeList.item(index).getLocalName().equals("host")) {
                check++;
            } else if (childNodeList.item(index).getLocalName().equals("user")) {
                check++;
            } else if (childNodeList.item(index).getLocalName().equals("password")) {
                check++;
            }
        }

        if (check != 3) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canHandle(final AbstractRelationshipTemplate relationshipTemplate) {
        // We handle only nodetypes at the moment
        return false;
    }

    @Override
    public String getID() {
        return PLUGIN_ID;
    }

}
