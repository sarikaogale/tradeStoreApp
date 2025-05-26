package com.datastore.trade.command.dto;

import com.datastore.trade.command.model.Trade;
import org.modelmapper.ModelMapper;

import java.text.ParseException;

public class TradeDtoUtil {

    public static Trade convertToEntity(TradeRequestDTO tradeRequestDTO, ModelMapper modelMapper) throws ParseException {
        Trade trade = modelMapper.map(tradeRequestDTO, Trade.class);

        return trade;
    }

}
