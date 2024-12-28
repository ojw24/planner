package com.ojw.planner.core.enumeration.planner.goal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ojw.planner.core.domain.DatePeriod;
import com.ojw.planner.core.enumeration.converter.EnumConverter;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import com.ojw.planner.core.util.EnumUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.ojw.planner.app.planner.goal.domain.QGoal.goal;

@Getter
@AllArgsConstructor
public enum GoalType implements EnumMapper {

    YEAR(
            "goal_type_001"
            , "year"
            , period -> goal.startDate.eq(period.getStartDate().withDayOfYear(1))
                    .and(goal.endDate.eq(period.getStartDate().withDayOfYear(period.getStartDate().lengthOfYear())))
    ),
    MONTH(
            "goal_type_002"
            ,"month"
            , period -> goal.startDate.eq(period.getStartDate().withDayOfMonth(1))
                    .and(goal.endDate.eq(period.getStartDate().withDayOfMonth(period.getStartDate().lengthOfMonth())))
    ),
    WEEK(
            "goal_type_003"
            ,"week"
            , period -> goal.startDate.eq(period.getStartDate())
                    .or(goal.endDate.eq(period.getEndDate()))
                    .or(goal.startDate.after(period.getStartDate()).and(goal.startDate.before(period.getEndDate())))
                    .or(goal.startDate.before(period.getStartDate()).and(goal.endDate.after(period.getStartDate())))

    ),
    DAY("goal_type_004","day", null);

    private final String code;

    private final String description;

    private Function<DatePeriod, BooleanExpression> expression;

    public BooleanExpression checkPeriod(DatePeriod period){
        return expression.apply(period);
    }

    @Converter(autoApply = true)
    static class GoalConverter extends EnumConverter<GoalType> {
        public GoalConverter() {
            super(GoalType.class);
        }
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GoalType ofCode(String code) {
        return EnumUtil.ofCode(GoalType.class, code);
    }

}
