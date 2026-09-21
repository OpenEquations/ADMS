package rw.adms.presentation.reports;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.reports.usecases.GenerateStockReportUseCase;
import rw.adms.application.reports.usecases.GetStockReportUseCase;
import rw.adms.application.reports.usecases.GetStockReportsUseCase;
import rw.adms.domain.reports.StockReport;
import rw.adms.domain.users.User;
import rw.adms.presentation.reports.dto.StockReportResponse;
import rw.adms.presentation.reports.dto.StockReportSummaryResponse;
import rw.adms.presentation.security.CurrentUser;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports/stock")
public class StockReportController {

    private final GenerateStockReportUseCase generateStockReportUseCase;
    private final GetStockReportsUseCase getStockReportsUseCase;
    private final GetStockReportUseCase getStockReportUseCase;

    public StockReportController(
            GenerateStockReportUseCase generateStockReportUseCase,
            GetStockReportsUseCase getStockReportsUseCase,
            GetStockReportUseCase getStockReportUseCase
    ) {
        this.generateStockReportUseCase = generateStockReportUseCase;
        this.getStockReportsUseCase = getStockReportsUseCase;
        this.getStockReportUseCase = getStockReportUseCase;
    }

    @PostMapping
    public ResponseEntity<StockReportResponse> generate(@CurrentUser User actingUser) {

        StockReport report = generateStockReportUseCase.execute(
                actingUser.getFirstName() + " " + actingUser.getLastName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/api/reports/stock/" + report.getId()))
                .body(StockReportResponse.from(report));
    }

    @GetMapping
    public ResponseEntity<List<StockReportSummaryResponse>> getAll() {

        List<StockReportSummaryResponse> reports = getStockReportsUseCase.execute()
                .stream()
                .map(StockReportSummaryResponse::from)
                .toList();

        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockReportResponse> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(
                StockReportResponse.from(getStockReportUseCase.execute(id))
        );
    }
}
