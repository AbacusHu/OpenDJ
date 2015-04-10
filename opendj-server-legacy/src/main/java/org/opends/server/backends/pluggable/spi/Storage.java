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
 *      Copyright 2014-2015 ForgeRock AS
 */
package org.opends.server.backends.pluggable.spi;

import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;

/**
 * This interface abstracts the underlying storage engine,
 * isolating the pluggable backend generic code from a particular storage engine implementation.
 */
public interface Storage extends Closeable
{
  /**
   * Starts the import operation.
   *
   * @return a new Importer object which must be closed to release all resources
   * @throws Exception
   *           if a problem occurs with the underlying storage engine
   * @see #close() to release all resources once import is finished
   */
  Importer startImport() throws Exception;

  /**
   * Opens the storage engine to allow executing operations on it.
   *
   * @throws Exception
   *           if a problem occurs with the underlying storage engine
   * @see #close() to release all resources once import is finished
   */
  void open() throws Exception;

  /**
   * Executes a read operation. In case of a read operation rollback, implementations must ensure
   * the read operation is retried until it succeeds.
   *
   * @param <T>
   *          type of the value returned
   * @param readOperation
   *          the read operation to execute
   * @return the value read by the read operation
   * @throws Exception
   *           if a problem occurs with the underlying storage engine
   */
  <T> T read(ReadOperation<T> readOperation) throws Exception;

  /**
   * Executes a write operation. In case of a write operation rollback, implementations must ensure
   * the write operation is retried until it succeeds.
   *
   * @param writeOperation
   *          the write operation to execute
   * @throws Exception
   *           if a problem occurs with the underlying storage engine
   */
  void write(WriteOperation writeOperation) throws Exception;

  /**
   * Returns a new writeable transaction.
   *
   * @return a new writeable transaction
   */
  WriteableTransaction getWriteableTransaction();

  /**
   * Remove all files for a backend of this storage.
   *
   * @throws StorageRuntimeException if removal fails
   */
  void removeStorageFiles() throws StorageRuntimeException;

  /**
   * Returns the current status of the storage.
   *
   * @return the current status of the storage
   */
  StorageStatus getStorageStatus();

  /**
   * Returns {@code true} if this storage supports backup and restore.
   *
   * @return {@code true} if this storage supports backup and restore.
   */
  boolean supportsBackupAndRestore();

  /**
   * Returns the file system directory in which any database files are located. This method is
   * called when performing backup and restore operations.
   *
   * @return The file system directory in which any database files are located.
   * @throws UnsupportedOperationException
   *           If backup and restore is not supported by this storage.
   */
  File getDirectory();

  /**
   * Returns a filename filter which selects the files to be included in a backup. This method is
   * called when performing backup operations.
   *
   * @return A filename filter which selects the files to be included in a backup.
   * @throws UnsupportedOperationException
   *           If backup and restore is not supported by this storage.
   */
  FilenameFilter getFilesToBackupFilter();

  /** {@inheritDoc} */
  @Override
  void close();
}