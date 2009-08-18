package org.opends.ldap.controls;



import static org.opends.messages.ProtocolMessages.ERR_PROXYAUTH2_CANNOT_DECODE_VALUE;
import static org.opends.messages.ProtocolMessages.ERR_PROXYAUTH2_CONTROL_NOT_CRITICAL;
import static org.opends.messages.ProtocolMessages.ERR_PROXYAUTH2_NO_CONTROL_VALUE;
import static org.opends.server.loggers.debug.DebugLogger.debugEnabled;
import static org.opends.server.loggers.debug.DebugLogger.getTracer;
import static org.opends.server.util.ServerConstants.OID_PROXIED_AUTH_V2;
import static org.opends.server.util.StaticUtils.getExceptionMessage;

import java.io.IOException;

import org.opends.asn1.ASN1;
import org.opends.asn1.ASN1Reader;
import org.opends.ldap.DecodeException;
import org.opends.messages.Message;
import org.opends.server.loggers.debug.DebugTracer;
import org.opends.server.types.ByteString;
import org.opends.server.types.DebugLogLevel;
import org.opends.server.util.Validator;
import org.opends.spi.ControlDecoder;
import org.opends.types.DN;



/**
 * This class implements version 2 of the proxied authorization control
 * as defined in RFC 4370. It makes it possible for one user to request
 * that an operation be performed under the authorization of another.
 * The target user is specified using an authorization ID, which may be
 * in the form "dn:" immediately followed by the DN of that user, or
 * "u:" followed by a user ID string.
 */
public class ProxiedAuthV2Control extends Control
{
  /**
   * ControlDecoder implentation to decode this control from a
   * ByteString.
   */
  private static final class Decoder implements
      ControlDecoder<ProxiedAuthV2Control>
  {
    /**
     * {@inheritDoc}
     */
    public ProxiedAuthV2Control decode(boolean isCritical,
        ByteString value) throws DecodeException
    {
      if (!isCritical)
      {
        Message message = ERR_PROXYAUTH2_CONTROL_NOT_CRITICAL.get();
        throw new DecodeException(message);
      }

      if (value == null)
      {
        Message message = ERR_PROXYAUTH2_NO_CONTROL_VALUE.get();
        throw new DecodeException(message);
      }

      ASN1Reader reader = ASN1.getReader(value);
      String authorizationID;

      try
      {
        if (reader.elementAvailable())
        {
          // Try the legacy encoding where the value is wrapped by an
          // extra octet string
          authorizationID = reader.readOctetStringAsString();
        }
        else
        {
          authorizationID = value.toString();
        }
      }
      catch (IOException e)
      {
        if (debugEnabled())
        {
          TRACER.debugCaught(DebugLogLevel.ERROR, e);
        }

        Message message =
            ERR_PROXYAUTH2_CANNOT_DECODE_VALUE
                .get(getExceptionMessage(e));
        throw new DecodeException(message, e);
      }

      return new ProxiedAuthV2Control(authorizationID);
    }



    public String getOID()
    {
      return OID_PROXIED_AUTH_V2;
    }

  }



  /**
   * The Control Decoder that can be used to decode this control.
   */
  public static final ControlDecoder<ProxiedAuthV2Control> DECODER =
      new Decoder();

  /**
   * The tracer object for the debug logger.
   */
  private static final DebugTracer TRACER = getTracer();

  // The authorization ID from the control value.
  private String authorizationID;



  /**
   * Creates a new instance of the proxied authorization v2 control with
   * the provided information.
   * 
   * @param authorizationDN
   *          The authorization DN.
   */
  public ProxiedAuthV2Control(DN authorizationDN)
  {
    super(OID_PROXIED_AUTH_V2, true);

    Validator.ensureNotNull(authorizationID);
    this.authorizationID = "dn:" + authorizationDN.toString();
  }



  /**
   * Creates a new instance of the proxied authorization v2 control with
   * the provided information.
   * 
   * @param authorizationID
   *          The authorization ID.
   */
  public ProxiedAuthV2Control(String authorizationID)
  {
    super(OID_PROXIED_AUTH_V2, true);

    Validator.ensureNotNull(authorizationID);
    this.authorizationID = authorizationID;
  }



  /**
   * Retrieves the authorization ID for this proxied authorization V2
   * control.
   * 
   * @return The authorization ID for this proxied authorization V2
   *         control.
   */
  public String getAuthorizationID()
  {
    return authorizationID;
  }



  @Override
  public ByteString getValue()
  {
    return ByteString.valueOf(authorizationID);
  }



  @Override
  public boolean hasValue()
  {
    return true;
  }



  public ProxiedAuthV2Control setAuthorizationID(DN authorizationDN)
  {
    Validator.ensureNotNull(authorizationDN);
    this.authorizationID = "dn:" + authorizationDN.toString();
    return this;
  }



  public ProxiedAuthV2Control setAuthorizationID(String authorizationID)
  {
    Validator.ensureNotNull(authorizationID);
    this.authorizationID = authorizationID;
    return this;
  }



  /**
   * Appends a string representation of this proxied auth v2 control to
   * the provided buffer.
   * 
   * @param buffer
   *          The buffer to which the information should be appended.
   */
  @Override
  public void toString(StringBuilder buffer)
  {
    buffer.append("ProxiedAuthorizationV2Control(oid=");
    buffer.append(getOID());
    buffer.append(", criticality=");
    buffer.append(isCritical());
    buffer.append(", authorizationDN=\"");
    buffer.append(authorizationID);
    buffer.append("\")");
  }
}
