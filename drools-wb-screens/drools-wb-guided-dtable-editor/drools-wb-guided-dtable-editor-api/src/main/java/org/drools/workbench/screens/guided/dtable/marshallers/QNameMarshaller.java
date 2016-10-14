package org.drools.workbench.screens.guided.dtable.marshallers;

import javax.xml.namespace.QName;

import org.jboss.errai.common.client.protocols.SerializationParts;
import org.jboss.errai.marshalling.client.api.MarshallingSession;
import org.jboss.errai.marshalling.client.api.annotations.ClientMarshaller;
import org.jboss.errai.marshalling.client.api.annotations.ServerMarshaller;
import org.jboss.errai.marshalling.client.api.json.EJValue;
import org.jboss.errai.marshalling.client.marshallers.AbstractNullableMarshaller;
import org.uberfire.rpc.SessionInfo;

@ClientMarshaller(QName.class)
@ServerMarshaller(QName.class)
public class QNameMarshaller extends AbstractNullableMarshaller<QName> {

    @Override
    public QName doNotNullDemarshall( final EJValue ejValue,
                                      final MarshallingSession marshallingSession ) {
        final String localPart = ejValue.isObject().get( "localPart" ).isString().stringValue();
        final String namespaceURI = ejValue.isObject().get( "namespaceURI" ).isString().stringValue();
        final String prefix = ejValue.isObject().get( "prefix" ).isString().stringValue();

        return new QName( namespaceURI,
                          localPart,
                          prefix );
    }

    @Override
    public String doNotNullMarshall( final QName qname,
                                     final MarshallingSession marshallingSession ) {
        return "{\"" + SerializationParts.ENCODED_TYPE + "\":\"" + SessionInfo.class.getName() + "\"," +
                "\"" + SerializationParts.OBJECT_ID + "\":\"" + qname.hashCode() + "\"," +
                "\"" + "localPart" + "\":\"" + qname.getLocalPart() + "\"," +
                "\"" + "namespaceURI" + "\":\"" + qname.getNamespaceURI() + "\"," +
                "\"" + "prefix" + "\":\"" + qname.getPrefix() + "\"}";
    }

    @Override
    public QName[] getEmptyArray() {
        return new QName[ 0 ];
    }

}
