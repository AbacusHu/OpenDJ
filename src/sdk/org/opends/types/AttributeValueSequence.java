/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2009 Sun Microsystems, Inc.
 */

package org.opends.types;



import java.util.Iterator;

import org.opends.server.types.ByteString;



/**
 * A readable sequence of attribute values.
 */
public interface AttributeValueSequence extends Iterable<ByteString>
{

  /**
   * Returns the attribute description associated with this attribute
   * value sequence. This includes the attribute name and any attribute
   * options.
   *
   * @return The attribute description associated with this attribute
   *         value sequence.
   */
  String getAttributeDescriptionAsString();



  /**
   * Indicates whether or not this attribute value sequence has any
   * values.
   *
   * @return {@code true} if this attribute value sequence does not
   *         contain any values, otherwise {@code false}.
   */
  boolean isEmpty();



  /**
   * Returns an {@code Iterator} over the values in this attribute value
   * sequence. The values are returned in the order in which they were
   * added to this attribute value sequence. Attempts to use the
   * iterator's {@code remove()} method will fail throwing an {@code
   * UnsupportedOperationException}.
   *
   * @return An {@code Iterator} over the attribute values in this
   *         attribute value sequence.
   */
  Iterator<ByteString> iterator();



  /**
   * Returns the number of attribute values in this attribute value
   * sequence.
   *
   * @return The number of attribute values in this attribute value
   *         sequence. .
   */
  int size();



  /**
   * Returns a string representation of this attribute value sequence.
   *
   * @return A string representation of this attribute value sequence.
   */
  String toString();



  /**
   * Appends a string representation of this attribute value sequence to
   * the provided {@code StringBuilder}.
   *
   * @param builder
   *          The {@code StringBuilder} into which a string
   *          representation of this attribute value sequence should be
   *          appended.
   * @return The updated {@code StringBuilder}.
   * @throws NullPointerException
   *           If {@code builder} was {@code null}.
   */
  StringBuilder toString(StringBuilder builder)
      throws NullPointerException;

}
