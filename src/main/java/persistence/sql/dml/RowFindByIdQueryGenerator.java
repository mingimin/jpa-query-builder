package persistence.sql.dml;

import persistence.sql.Dialect;
import persistence.sql.QueryBuilder;
import persistence.sql.TableFieldUtil;
import persistence.sql.TableQueryUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class RowFindByIdQueryGenerator extends QueryBuilder {
    final private LinkedHashSet<Object> primaryKeys = new LinkedHashSet<>();

    public RowFindByIdQueryGenerator(Dialect dialect) {
        super(dialect);
    }

    public String generateSQLQuery(Object object) {
        return "SELECT " +
            TableQueryUtil.getSelectedColumns(object) +
            " FROM " +
            TableQueryUtil.getTableName(object.getClass()) +
            " WHERE " +
            whereClause(object.getClass()) +
            ";";
    }

    public RowFindByIdQueryGenerator findBy(Object... primaryKeyValues){
        Collections.addAll(this.primaryKeys, primaryKeyValues);
        return this;
    }

    private String whereClause(Class<?> clazz) {
        Field primaryKeyField = TableFieldUtil.getPrimaryKeyField(clazz);
        String primaryKeyColumName = TableFieldUtil.replaceNameByBacktick(TableFieldUtil.getColumnName(primaryKeyField));
        switch (primaryKeys.size()) {
            case 0:
            case 1:
                return primaryKeyColumName + " = " + primaryKeys.stream().findFirst().orElse(0L);
            default:
                return primaryKeyColumName + " in (" + primaryKeys.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
        }
    }
}
