package com.ojw.planner.core.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;

public class RepositoryUtil {

    @Deprecated //querydsl 취약점 개선 시 해제 or 필요할 시, 검증 로직 추가 후 해제
    public static <T> OrderSpecifier[] getOrderSpecifier(Sort sort, T entity) {

        return sort.stream().map
                        (o -> {
                            //asc인지, desc인지 확인
                            Order direction = o.isAscending() ? Order.ASC : Order.DESC;

                            PathBuilder orderExpression = new PathBuilder(entity.getClass(), getQName(entity));

                            //getProperty를 통해 sort 대상 컬럼 구함
                            return new OrderSpecifier<>(direction, orderExpression.get(o.getProperty()));
                        })
                .toArray(OrderSpecifier[]::new);

    }

    private static <T> String getQName(T entity) {

        Field[] fields = entity.getClass().getDeclaredFields();

        String qName = entity.getClass().getSimpleName();
        qName = qName.substring(0, 1).toLowerCase() + qName.substring(1);

        //클래스명과 동일한 컬럼명 있을 경우 Qclass variable이 자동으로 1붙여서 생성하기 때문에
        for (Field field : fields) {

            field.setAccessible(true);
            if(field.getName().equalsIgnoreCase(qName)) {
                qName = qName + 1;
                break;
            }

        }

        return qName;

    }

}
