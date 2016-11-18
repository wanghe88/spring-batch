/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.item.database.builder;

import java.util.List;
import javax.sql.DataSource;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.AbstractCursorItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.support.ListPreparedStatementSetter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.ArgumentTypePreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Builder for the {@link JdbcCursorItemReader}
 *
 * @author Michael Minella
 * @since 4.0
 */
public class JdbcCursorItemReaderBuilder<T> {

	private DataSource dataSource;

	private int fetchSize = AbstractCursorItemReader.VALUE_NOT_SET;

	private int maxRows = AbstractCursorItemReader.VALUE_NOT_SET;

	private int queryTimeout = AbstractCursorItemReader.VALUE_NOT_SET;

	private int currentItemCount = 0;

	private int maxItemCount = Integer.MAX_VALUE;

	private boolean ignoreWarnings;

	private boolean verifyCursorPosition;

	private boolean driverSupportsAbsolute;

	private boolean useSharedExtendedConnection;

	private boolean saveState = true;

	private PreparedStatementSetter preparedStatementSetter;

	private String sql;

	private String name;

	private RowMapper<T> rowMapper;

	/**
	 * The {@link DataSource} to read from
	 *
	 * @param dataSource a relational data base
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setDataSource(DataSource)
	 */
	public JdbcCursorItemReaderBuilder<T> dataSource(DataSource dataSource) {
		this.dataSource = dataSource;

		return this;
	}

	/**
	 * A hint to the driver as to how many rows to return with each fetch.
	 *
	 * @param fetchSize the hint
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setFetchSize(int)
	 */
	public JdbcCursorItemReaderBuilder<T> fetchSize(int fetchSize) {
		this.fetchSize = fetchSize;

		return this;
	}

	/**
	 * The max number of rows the {@link java.sql.ResultSet} can contain
	 *
	 * @param maxRows the max
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setMaxRows(int)
	 */
	public JdbcCursorItemReaderBuilder<T> maxRows(int maxRows) {
		this.maxRows = maxRows;

		return this;
	}

	/**
	 * The time in milliseconds for the query to timeout
	 *
	 * @param queryTimeout timeout
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setQueryTimeout(int)
	 */
	public JdbcCursorItemReaderBuilder<T> queryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;

		return this;
	}

	/**
	 * The index of the first record to begin reading from.  Overridden if a previous value
	 * is provided via the {@link org.springframework.batch.item.ExecutionContext} on
	 * {@link org.springframework.batch.item.ItemStream#open(ExecutionContext)}
	 *
	 * @param currentItemCount current index
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setCurrentItemCount(int)
	 */
	public JdbcCursorItemReaderBuilder<T> currentItemCount(int currentItemCount) {
		this.currentItemCount = currentItemCount;

		return this;
	}

	/**
	 * The max number of items to be read.  Overriden if a previous value is povided via
	 * the {@link ExecutionContext} on {@link org.springframework.batch.item.ItemStream#open}
	 *
	 * @param maxItemCount count
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setMaxItemCount(int)
	 */
	public JdbcCursorItemReaderBuilder<T> maxItemCount(int maxItemCount) {
		this.maxItemCount = maxItemCount;

		return this;
	}

	/**
	 * Indicates if the state of the reader should be persisted in the
	 * {@link ExecutionContext}.  Defaults to true.
	 *
	 * @param saveState indicator.  Defaults to true
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setSaveState(boolean)
	 */
	public JdbcCursorItemReaderBuilder<T> saveState(boolean saveState) {
		this.saveState = saveState;

		return this;
	}

	public JdbcCursorItemReaderBuilder<T> ignoreWarnings(boolean ignoreWarnings) {
		this.ignoreWarnings = ignoreWarnings;

		return this;
	}

	/**
	 * Indicates if the reader should verify the current position of the
	 * {@link java.sql.ResultSet} after being passed to the {@link RowMapper}.  Defaults
	 * to true.
	 *
	 * @param verifyCursorPosition indicator
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setVerifyCursorPosition(boolean)
	 */
	public JdbcCursorItemReaderBuilder<T> verifyCursorPosition(boolean verifyCursorPosition) {
		this.verifyCursorPosition = verifyCursorPosition;

		return this;
	}

	/**
	 * Indicates if the JDBC driver supports setting the absolute row on the
	 * {@link java.sql.ResultSet}.
	 *
	 * @param driverSupportsAbsolute indicator
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setDriverSupportsAbsolute(boolean)
	 */
	public JdbcCursorItemReaderBuilder<T> driverSupportsAbsolute(boolean driverSupportsAbsolute) {
		this.driverSupportsAbsolute = driverSupportsAbsolute;

		return this;
	}

	/**
	 * Indicates that the connection used for the cursor is being used by all other
	 * processing, therefor part of the same transaction.
	 *
	 * @param useSharedExtendedConnection indicator
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setUseSharedExtendedConnection(boolean)
	 */
	public JdbcCursorItemReaderBuilder<T> useSharedExtendedConnection(boolean useSharedExtendedConnection) {
		this.useSharedExtendedConnection = useSharedExtendedConnection;

		return this;
	}

	/**
	 * Configures the provided {@link PreparedStatementSetter} to be used to populate any
	 * arguments in the SQL query to be executed for the reader.
	 *
	 * @param preparedStatementSetter setter
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setPreparedStatementSetter(PreparedStatementSetter)
	 */
	public JdbcCursorItemReaderBuilder<T> preparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
		this.preparedStatementSetter = preparedStatementSetter;

		return this;
	}

	/**
	 * Configures a {@link PreparedStatementSetter} that will use the array as the values
	 * to be set on the query to be executed for this reader.
	 *
	 * @param args values to set on the reader query
	 * @return this instance for method chaining
	 */
	public JdbcCursorItemReaderBuilder<T> queryArguments(Object[] args) {
		this.preparedStatementSetter = new ArgumentPreparedStatementSetter(args);

		return this;
	}

	/**
	 * Configures a {@link PreparedStatementSetter} that will use the Object [] as the
	 * values to be set on the query to be executed for this reader.  The int[] will
	 * provide the types ({@link java.sql.Types}) for each of the values provided.
	 *
	 * @param args values to set on the query
	 * @param types the type for each value in the args array
	 * @return this instance for method chaining
	 */
	public JdbcCursorItemReaderBuilder<T> queryArguments(Object[] args, int[] types) {
		this.preparedStatementSetter = new ArgumentTypePreparedStatementSetter(args, types);

		return this;
	}

	/**
	 * Configures a {@link PreparedStatementSetter} that will use the List as the values
	 * to be set on the query to be executed for this reader.
	 *
	 * @param args values to set on the query
	 * @return this instance for method chaining
	 * @throws Exception from {@link InitializingBean#afterPropertiesSet()}
	 */
	public JdbcCursorItemReaderBuilder<T> queryArguments(List<?> args) throws Exception {
		ListPreparedStatementSetter listPreparedStatementSetter = new ListPreparedStatementSetter(args);

		listPreparedStatementSetter.afterPropertiesSet();

		this.preparedStatementSetter = listPreparedStatementSetter;

		return this;
	}

	/**
	 * The query to be executed for this reader
	 *
	 * @param sql query
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setSql(String)
	 */
	public JdbcCursorItemReaderBuilder<T> sql(String sql) {
		this.sql = sql;

		return this;
	}

	/**
	 * The {@link RowMapper} used to map the results of the cursor to each item.
	 *
	 * @param rowMapper {@link RowMapper}
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setRowMapper(RowMapper)
	 */
	public JdbcCursorItemReaderBuilder<T> rowMapper(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;

		return this;
	}

	/**
	 * A name used to prevent key collisions while saving state in the
	 * {@link ExecutionContext}.
	 *
	 * @param name unique name for this reader instance
	 * @return this instance for method chaining
	 * @see JdbcCursorItemReader#setName(String)
	 */
	public JdbcCursorItemReaderBuilder<T> name(String name) {
		this.name = name;

		return this;
	}

	/**
	 * Validates configuration and builds a new reader instance.
	 *
	 * @return a fully constructed {@link JdbcCursorItemReader}
	 */
	public JdbcCursorItemReader<T> build() {
		if(this.saveState) {
			Assert.hasText(this.name,
					"A name is required when saveSate is set to true");
		}

		Assert.hasText(this.sql, "A query is required");
		Assert.notNull(this.dataSource, "A datasource is required");
		Assert.notNull(this.rowMapper, "A rowmapper is required");

		JdbcCursorItemReader<T> reader = new JdbcCursorItemReader<>();

		if(StringUtils.hasText(this.name)) {
			reader.setName(this.name);
		}

		reader.setSaveState(this.saveState);
		reader.setPreparedStatementSetter(this.preparedStatementSetter);
		reader.setRowMapper(this.rowMapper);
		reader.setSql(this.sql);
		reader.setCurrentItemCount(this.currentItemCount);
		reader.setDataSource(this.dataSource);
		reader.setDriverSupportsAbsolute(this.driverSupportsAbsolute);
		reader.setFetchSize(this.fetchSize);
		reader.setIgnoreWarnings(this.ignoreWarnings);
		reader.setMaxItemCount(this.maxItemCount);
		reader.setMaxRows(this.maxRows);
		reader.setQueryTimeout(this.queryTimeout);
		reader.setUseSharedExtendedConnection(this.useSharedExtendedConnection);
		reader.setVerifyCursorPosition(this.verifyCursorPosition);

		return reader;
	}
 }
