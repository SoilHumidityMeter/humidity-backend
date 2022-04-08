package com.soilhumidity.backend.dto.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soilhumidity.backend.enums.ELogLevel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class LogFileRequest {

    @Getter
    @ApiModelProperty
    private Date date;

    @Getter
    @ApiModelProperty
    private String time;

    @Getter
    @ApiModelProperty
    private ELogLevel level;

    @ApiParam(hidden = true)
    public boolean getByLevel() {
        return level != null;
    }


    @ApiParam(hidden = true)
    @JsonIgnore
    public boolean isByTime() {
        return time != null && !getTimes().isEmpty();
    }

    @ApiParam(hidden = true)
    @JsonIgnore
    public List<Integer> getTimes() {
        var spl = time.split(":");
        var list = new ArrayList<Integer>();

        try {
            list.add(Integer.valueOf(spl[0]));
            list.add(Integer.valueOf(spl[1]));
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @ApiParam(hidden = true)
    @JsonIgnore
    public String getFileName() {

        if (getDate() == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return String.format("logs/spring_%s_%d.log.gz",
                dateFormat.format(getDate()), 0);
    }
}
