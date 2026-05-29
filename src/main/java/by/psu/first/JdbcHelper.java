package by.psu.first;

import org.springframework.stereotype.Service; // Импортируем Спринг Сервис
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service // Навешиваем аннотацию, чтобы Spring увидел этот класс

public class JdbcHelper {

    public List<Excursion> getAll() {
        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, duration_day FROM excursion";
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
    }

    public Excursion getById(Integer id) {
        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, duration_day FROM excursion WHERE id = ?";

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
        String sql = "UPDATE excursion SET name=?, price=?, \"from\"=?, \"to\"=?, guide_name=?, duration_day=? WHERE id=?";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, excursion.getName());
            statement.setBigDecimal(2, excursion.getPrice());
            statement.setDate(3, java.sql.Date.valueOf(excursion.getFrom()));
            statement.setDate(4, java.sql.Date.valueOf(excursion.getTo()));
            statement.setString(5, excursion.getWhere());
            statement.setInt(6, excursion.getDay());
            statement.setInt(7, excursion.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении экскурсии", e);
        }
    }

    public void create(Excursion excursion) {
        String sql = "INSERT INTO excursion (name, price, \"from\", \"to\", guide_name, duration_day) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, excursion.getName());
            statement.setBigDecimal(2, excursion.getPrice());
            statement.setDate(3, java.sql.Date.valueOf(excursion.getFrom()));
            statement.setDate(4, java.sql.Date.valueOf(excursion.getTo()));
            statement.setString(5, excursion.getWhere()); // используем getWhere как в вашем update
            statement.setInt(6, excursion.getDay());

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
        return new Excursion(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getDate("from").toLocalDate(),
                rs.getDate("to").toLocalDate(),
                rs.getString("guide_name"),
                rs.getInt("duration_day")
        );
    }
}
