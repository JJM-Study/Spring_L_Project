package org.example.myproject.shop.server;

import org.example.myproject.gis.dto.OutputGISDto;

import org.example.myproject.shop.dto.ShopDTO;
import org.example.myproject.shop.mapper.ShopMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;


@Service
public class ShopService {

    @Autowired
    private ShopMapper shopMapper;

    private static final Logger logger = LogManager.getLogger(ShopService.class);

    public Mono<List<ShopDTO>> selectShopId(List<String> externalIds) {

        logger.info("Service Input: {}", externalIds);

        return Mono.fromCallable( () ->
                shopMapper.selectShopId(externalIds)
                ).subscribeOn(Schedulers.boundedElastic())
                .doOnNext(e -> logger.info("test: {}", e));
    }
}
