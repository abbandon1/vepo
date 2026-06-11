package by.psu.first;

import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Set;

@Service
public class JdbcHelper {

    /*public List<Excursion> getAll() {
        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, duration_day, excursion_type, lunch_included FROM excursion";
        List<Excursion> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapResultSetToExcursion(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех экскурсий", e);
        }
        return result;
    }*/

    public List<Excursion> getAll(Pageable pageable) {

        String sortOrder = "id ASC";

        if (pageable.getSort().isSorted()) {

            org.springframework.data.domain.Sort.Order order = pageable.getSort().iterator().next();
            String property = order.getProperty();


            String columnName;
            switch (property) {
                case "name": columnName = "name"; break;
                case "price": columnName = "price"; break;
                case "from": columnName = "\"from\""; break;
                case "to": columnName = "\"to\""; break;
                case "guideName": columnName = "guide_name"; break;
                case "excursionType": columnName = "excursion_type"; break;
                case "lunchIncluded": columnName = "lunch_included"; break;
                default: columnName = "id"; break;
            }


            String direction = "ASC";
            if (order.getDirection() == org.springframework.data.domain.Sort.Direction.DESC) {
                direction = "DESC";
            }

            sortOrder = columnName + " " + direction;
        }


        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, duration_day, excursion_type, lunch_included " +
                "FROM excursion " +
                "ORDER BY " + sortOrder + " " +
                "LIMIT ? OFFSET ?";

        List<Excursion> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            int limit = pageable.getPageSize();
            long offset = pageable.getOffset();

            statement.setInt(1, limit);
            statement.setLong(2, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapResultSetToExcursion(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении страницы экскурсий", e);
        }
        return result;
    }

    public Excursion getById(Integer id) {
        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, duration_day, excursion_type, lunch_included FROM excursion WHERE id = ?";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToExcursion(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске экскурсии по ID: " + id, e);
        }
        return null;
    }

    public void update(Excursion excursion) {
        String sql = "UPDATE excursion SET name=?, price=?, \"from\"=?, \"to\"=?, guide_name=?, duration_day=?, excursion_type=?, lunch_included=? WHERE id=?";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, excursion.getName());
            statement.setBigDecimal(2, excursion.getPrice());
            statement.setDate(3, java.sql.Date.valueOf(excursion.getFrom()));
            statement.setDate(4, java.sql.Date.valueOf(excursion.getTo()));
            statement.setString(5, excursion.getWhere());
            statement.setInt(6, excursion.getDay());
            statement.setString(7, excursion.getExcursionType());
            statement.setBoolean(8, excursion.isLunchIncluded());
            statement.setInt(9, excursion.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении экскурсии", e);
        }
    }

    public void create(Excursion excursion) {
        String sql = "INSERT INTO excursion (name, price, \"from\", \"to\", guide_name, duration_day, excursion_type, lunch_included) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, excursion.getName());
            statement.setBigDecimal(2, excursion.getPrice());
            statement.setDate(3, java.sql.Date.valueOf(excursion.getFrom()));
            statement.setDate(4, java.sql.Date.valueOf(excursion.getTo()));
            statement.setString(5, excursion.getWhere());
            statement.setInt(6, excursion.getDay());
            statement.setString(7, excursion.getExcursionType());
            statement.setBoolean(8, excursion.isLunchIncluded());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании экскурсии", e);
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM excursion WHERE id = ?";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении экскурсии с ID: " + id, e);
        }
    }

    private Excursion mapResultSetToExcursion(ResultSet rs) throws SQLException {
        Excursion excursion = new Excursion(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getDate("from").toLocalDate(),
                rs.getDate("to").toLocalDate(),
                rs.getString("guide_name"),
                rs.getInt("duration_day")
        );

        excursion.setExcursionType(rs.getString("excursion_type"));
        excursion.setLunchIncluded(rs.getBoolean("lunch_included"));
        return excursion;
    }
}
