/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2008-2009 Sun Microsystems, Inc.
 *      Portions Copyright 2014 ForgeRock AS
 */
package org.opends.server.api;

import java.util.Collection;

import org.forgerock.opendj.ldap.ByteSequence;
import org.forgerock.opendj.ldap.DecodeException;
import org.forgerock.opendj.ldap.spi.IndexQueryFactory;
import org.forgerock.opendj.ldap.spi.IndexingOptions;

/**
 * This interface defines the set of methods that must be
 * implemented by a Directory Server module that implements an
 * Extensible matching rule.
 */
@org.opends.server.types.PublicAPI(
    stability = org.opends.server.types.StabilityLevel.VOLATILE,
    mayInstantiate = false,
    mayExtend = true,
    mayInvoke = false)
public interface ExtensibleMatchingRule extends MatchingRule
{
  /**
   * Returns a collection of extensible indexers associated with this matching
   * rule.
   *
   * @param indexingOptions
   *          The indexing options to be used by this matching rule.
   * @return The collection of extensible indexers associated with this matching
   *         rule.
   */
  Collection<ExtensibleIndexer> getIndexers(IndexingOptions indexingOptions);



  /**
   * Returns an index query appropriate for the provided attribute
   * value assertion.
   *
   * @param <T>
   *          The type of index query created by the {@code factory}.
   * @param assertionValue
   *          The attribute value assertion.
   * @param factory
   *          The index query factory which should be used to
   *          construct the index query.
   * @return The index query appropriate for the provided attribute
   *         value assertion.
   * @throws DecodeException
   *           If an error occurs while generating the index query.
   */
  <T> T createIndexQuery(ByteSequence assertionValue,
      IndexQueryFactory<T> factory) throws DecodeException;
}