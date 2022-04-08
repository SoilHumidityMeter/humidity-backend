package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.LocationDto;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.soilhumidity.backend.repository.LocationRepository;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = LocationController.TAG)
@RequestMapping(LocationController.TAG)
@ApiInformation(tag = LocationController.TAG, description = "Locations related endpoints")
@AllArgsConstructor
public class LocationController {

    protected static final String TAG = "locations";

    private final LocationRepository locationRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public List<LocationDto> getChildLocation(Long id) {
        return locationRepository.findAllByParentId(id);
    }


    @PostMapping("migrate")
    @RolesAllowed(ERole.SYSADMIN)
    public void migrateLocations(@RequestPart MultipartFile file,
                                 @RequestParam String country,
                                 HttpServletResponse response) {

        var countryLoc = 1;
        int cityLoc = 0;
        int townLoc = 0;
        int count = 1;
        StringBuilder myString = new StringBuilder(String.format("(%s,\"%s\",0,NULL),%n", count, country.strip()));
        count++;

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);
            var cityName = "";
            var townName = "";
            var first = true;
            for (Row row : sheet) {
                if (!first) {
                    if (!cityName.equals(row.getCell(0).getStringCellValue().strip())) {
                        cityName = row.getCell(0).getStringCellValue().strip();
                        myString.append(String.format("(%s,\"%s\",1,%s),%n", count, cityName, countryLoc));
                        cityLoc = count;
                        count++;
                    }
                    if (!townName.equals(row.getCell(1).getStringCellValue().strip())) {
                        townName = row.getCell(1).getStringCellValue().strip();
                        myString.append(String.format("(%s,\"%s\",2,%s),%n", count, townName, cityLoc));
                        townLoc = count;
                        count++;
                    }
                    myString.append(String.format("(%s,\"%s\",3,%s),%n", count, row.getCell(3).getStringCellValue().strip(), townLoc));
                    count++;

                } else {
                    first = false;
                }
            }
        } catch (IOException ignored) {

        }

        response.setContentType("text/plain; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=myFile.txt");

        try {
            var out = response.getWriter();
            out.println(myString);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
