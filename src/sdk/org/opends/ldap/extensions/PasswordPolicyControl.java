package org.opends.ldap.extensions;



import static org.opends.messages.ProtocolMessages.*;
import static org.opends.server.loggers.debug.DebugLogger.*;
import static org.opends.server.util.ServerConstants.*;
import static org.opends.server.util.StaticUtils.*;

import java.io.IOException;

import org.opends.asn1.ASN1;
import org.opends.asn1.ASN1Reader;
import org.opends.asn1.ASN1Writer;
import org.opends.ldap.Control;
import org.opends.ldap.ControlDecoder;
import org.opends.ldap.DecodeException;
import org.opends.messages.Message;
import org.opends.server.loggers.debug.DebugTracer;
import org.opends.server.types.ByteString;
import org.opends.server.types.ByteStringBuilder;
import org.opends.server.types.DebugLogLevel;
import org.opends.server.util.Validator;



/**
 * This class implements the password policy control defined in
 * draft-behera-ldap-password-policy.
 */
public class PasswordPolicyControl
{
  /**
   * This class implements the password policy request control defined
   * in draft-behera-ldap-password-policy. It does not have a value.
   */
  public static class Request extends Control
  {
    public Request()
    {
      super(OID_PASSWORD_POLICY_CONTROL, false);
    }



    public Request(boolean isCritical)
    {
      super(OID_PASSWORD_POLICY_CONTROL, isCritical);
    }



    @Override
    public ByteString getValue()
    {
      return null;
    }



    @Override
    public boolean hasValue()
    {
      return false;
    }



    @Override
    public void toString(StringBuilder buffer)
    {
      buffer.append("PasswordPolicyRequestControl(oid=");
      buffer.append(getOID());
      buffer.append(", criticality=");
      buffer.append(isCritical());
      buffer.append(")");
    }
  }

  /**
   * This class implements the password policy response control defined
   * in draft-behera-ldap-password-policy. The value may have zero, one,
   * or two elements, which may include flags to indicate a warning
   * and/or an error.
   */
  public static class Response extends Control
  {
    // The warning value for this password policy response control.
    private int warningValue;

    // The error type for this password policy response control.
    private PasswordPolicyErrorType errorType;

    // The warning type for the password policy response control.
    private PasswordPolicyWarningType warningType;



    /**
     * Creates a new instance of the password policy response control
     * with the default OID and criticality, and without either a
     * warning or an error flag.
     */
    public Response()
    {
      this(false);
    }



    /**
     * Creates a new instance of the password policy response control
     * with the default OID and criticality, and without either a
     * warning or an error flag.
     * 
     * @param isCritical
     *          Indicates whether support for this control should be
     *          considered a critical part of the client processing.
     */
    public Response(boolean isCritical)
    {
      super(OID_PASSWORD_POLICY_CONTROL, isCritical);

      warningType = null;
      errorType = null;
      warningValue = -1;
    }



    /**
     * Retrieves the password policy error type contained in this
     * control.
     * 
     * @return The password policy error type contained in this control,
     *         or <CODE>null</CODE> if there is no error type.
     */
    public PasswordPolicyErrorType getErrorType()
    {
      return errorType;
    }



    @Override
    public ByteString getValue()
    {
      ByteStringBuilder buffer = new ByteStringBuilder();
      ASN1Writer writer = ASN1.getWriter(buffer);
      try
      {
        writer.writeStartSequence();
        if (warningType != null)
        {
          // Just write the CHOICE element as a single element SEQUENCE.
          writer.writeStartSequence(TYPE_WARNING_ELEMENT);
          writer.writeInteger((byte) (0x80 | warningType.intValue()),
              warningValue);
          writer.writeEndSequence();
        }

        if (errorType != null)
        {
          writer.writeInteger(TYPE_ERROR_ELEMENT, errorType.intValue());
        }
        writer.writeEndSequence();
        return buffer.toByteString();
      }
      catch (IOException ioe)
      {
        // This should never happen unless there is a bug somewhere.
        throw new RuntimeException(ioe);
      }
    }



    /**
     * Retrieves the password policy warning type contained in this
     * control.
     * 
     * @return The password policy warning type contained in this
     *         control, or <CODE>null</CODE> if there is no warning
     *         type.
     */
    public PasswordPolicyWarningType getWarningType()
    {
      return warningType;
    }



    /**
     * Retrieves the password policy warning value for this control. The
     * value is undefined if there is no warning type.
     * 
     * @return The password policy warning value for this control.
     */
    public int getWarningValue()
    {
      return warningValue;
    }



    @Override
    public boolean hasValue()
    {
      return true;
    }



    public Response setError(PasswordPolicyErrorType error)
    {
      Validator.ensureNotNull(error);
      this.errorType = error;
      return this;
    }



    public Response setWarning(PasswordPolicyWarningType type, int value)
    {
      Validator.ensureNotNull(type);
      this.warningType = type;
      this.warningValue = value;
      return this;
    }



    /**
     * Appends a string representation of this password policy response
     * control to the provided buffer.
     * 
     * @param buffer
     *          The buffer to which the information should be appended.
     */
    @Override
    public void toString(StringBuilder buffer)
    {
      buffer.append("PasswordPolicyResponseControl(oid=");
      buffer.append(getOID());
      buffer.append(", criticality=");
      buffer.append(isCritical());
      buffer.append(", warningType=");
      buffer.append(warningType);
      buffer.append(", warningValue=");
      buffer.append(warningValue);
      buffer.append(", errorType=");
      buffer.append(errorType);
      buffer.append(")");
    }
  }

  /**
   * ControlDecoder implentation to decode this control from a
   * ByteString.
   */
  private static final class RequestDecoder implements
      ControlDecoder<Request>
  {
    /**
     * {@inheritDoc}
     */
    public Request decode(boolean isCritical, ByteString value)
        throws DecodeException
    {
      if (value != null)
      {
        Message message = ERR_PWPOLICYREQ_CONTROL_HAS_VALUE.get();
        throw new DecodeException(message);
      }

      return new Request(isCritical);
    }



    public String getOID()
    {
      return OID_PASSWORD_POLICY_CONTROL;
    }
  }

  /**
   * ControlDecoder implentation to decode this control from a
   * ByteString.
   */
  private final static class ResponseDecoder implements
      ControlDecoder<Response>
  {
    /**
     * {@inheritDoc}
     */
    public Response decode(boolean isCritical, ByteString value)
        throws DecodeException
    {
      if (value == null)
      {
        // The response control must always have a value.
        Message message = ERR_PWPOLICYRES_NO_CONTROL_VALUE.get();
        throw new DecodeException(message);
      }

      ASN1Reader reader = ASN1.getReader(value);
      try
      {
        PasswordPolicyWarningType warningType = null;
        PasswordPolicyErrorType errorType = null;
        int warningValue = -1;

        reader.readStartSequence();

        if (reader.hasNextElement()
            && (reader.peekType() == TYPE_WARNING_ELEMENT))
        {
          // Its a CHOICE element. Read as sequence to retrieve
          // nested element.
          reader.readStartSequence();
          warningType =
              PasswordPolicyWarningType.valueOf(0x7F & reader
                  .peekType());
          warningValue = (int) reader.readInteger();
          if (warningType == null)
          {
            Message message =
                ERR_PWPOLICYRES_INVALID_WARNING_TYPE
                    .get(byteToHex(reader.peekType()));
            throw new DecodeException(message);
          }
          reader.readEndSequence();
        }
        if (reader.hasNextElement()
            && (reader.peekType() == TYPE_ERROR_ELEMENT))
        {
          int errorValue = (int) reader.readInteger();
          errorType = PasswordPolicyErrorType.valueOf(errorValue);
          if (errorType == null)
          {
            Message message =
                ERR_PWPOLICYRES_INVALID_ERROR_TYPE.get(errorValue);
            throw new DecodeException(message);
          }
        }

        reader.readEndSequence();

        Response response = new Response(isCritical);
        if (warningType != null)
        {
          response.setWarning(warningType, warningValue);
        }
        if (errorType != null)
        {
          response.setError(errorType);
        }
        return response;
      }
      catch (IOException e)
      {
        if (debugEnabled())
        {
          TRACER.debugCaught(DebugLogLevel.ERROR, e);
        }

        Message message =
            ERR_PWPOLICYRES_DECODE_ERROR.get(getExceptionMessage(e));
        throw new DecodeException(message);
      }
    }



    public String getOID()
    {
      return OID_ACCOUNT_USABLE_CONTROL;
    }

  }



  /**
   * The tracer object for the debug logger.
   */
  private static final DebugTracer TRACER = getTracer();

  /**
   * The BER type value for the warning element of the control value.
   */
  private static final byte TYPE_WARNING_ELEMENT = (byte) 0xA0;

  /**
   * The BER type value for the error element of the control value.
   */
  private static final byte TYPE_ERROR_ELEMENT = (byte) 0x81;

  /**
   * The Control Decoder that can be used to decode the request control.
   */
  public static final ControlDecoder<Request> REQUEST_DECODER =
      new RequestDecoder();

  /**
   * The Control Decoder that can be used to decode the response
   * control.
   */
  public static final ControlDecoder<Response> RESPONSE_DECODER =
      new ResponseDecoder();
}
