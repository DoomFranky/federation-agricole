package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CollectivityLocalStatisticsDTO;
import school.hei.exam.agriculturalfederation.dto.CollectivityOverallStatisticsDTO;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.StatisticsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final CollectivityRepository collectivityRepository;

    public StatisticsService(StatisticsRepository statisticsRepository, CollectivityRepository collectivityRepository) {
        this.statisticsRepository = statisticsRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityLocalStatisticsDTO> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        validateDateRange(from, to);
        return statisticsRepository.findLocalStatistics(collectivityId, from, to);
    }

    public List<CollectivityOverallStatisticsDTO> getOverallStatistics(LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        return statisticsRepository.findOverallStatistics(from, to);
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are required");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date must be before 'to' date");
        }
    }
}
