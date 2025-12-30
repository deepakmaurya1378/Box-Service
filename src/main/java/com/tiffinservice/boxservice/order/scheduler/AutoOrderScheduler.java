package com.tiffinservice.boxservice.order.scheduler;

import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import com.tiffinservice.boxservice.order.service.AutoOrderService;
import com.tiffinservice.boxservice.user.entity.UserVendorPreference;
import com.tiffinservice.boxservice.user.repository.UserVendorPreferenceRepository;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoOrderScheduler {

    private static final int EXECUTION_WINDOW_MINUTES = 5;

    private final UserVendorPreferenceRepository preferenceRepo;
    private final AutoOrderService autoOrderService;

    @Scheduled(cron = "0 */5 * * * *")
    public void processAutoOrders() {

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        for (ShiftType shift : ShiftType.values()) {

            List<UserVendorPreference> preferences =
                    preferenceRepo.findByShiftTypeAndPreferenceType(shift, PreferenceType.DEFAULT);

            for (UserVendorPreference pref : preferences) {

                Vendor vendor = pref.getVendor();

                LocalTime mealStartTime = getVendorMealStartTime(vendor, shift);
                if (mealStartTime == null) {
                    continue; }

                LocalDateTime autoOrderTime = today.atTime(mealStartTime).minusHours(4);

                if (isWithinExecutionWindow(now, autoOrderTime)) {
                    log.info("AutoOrderScheduler triggered | userId={} vendorId={} shift={}", pref.getUser().getId(), vendor.getId(), shift);
                    autoOrderService.createAutoOrder(pref.getUser(), vendor, shift, today, autoOrderTime);
                }
            }
        }
    }

    private boolean isWithinExecutionWindow(LocalDateTime now, LocalDateTime target) {
        return now.isAfter(target.minusMinutes(EXECUTION_WINDOW_MINUTES)) && now.isBefore(target.plusMinutes(EXECUTION_WINDOW_MINUTES));
    }

    private LocalTime getVendorMealStartTime(Vendor vendor, ShiftType shift) {
        return switch (shift) {
            case BREAKFAST -> vendor.getBreakfastStartTime();
            case LUNCH -> vendor.getLunchStartTime();
            case DINNER -> vendor.getDinnerStartTime();
        };
    }
}
