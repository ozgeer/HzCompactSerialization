package com.github.ozgeer.smf.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BitRate {
	private static final long serialVersionUID = 859983937736034231L;

	private static final BigInteger N5G_NAS_MAX_SCALAR_VALUE = BigInteger.valueOf(65535); // 2-bytes
	private static final BitRate ZERO = new BitRate(BigInteger.ZERO, Unit.BPS);
	@JsonProperty("value")
	private final BigInteger value;
	@JsonProperty("unit")
	private final Unit unit;

	public BitRate(BigInteger value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	public enum Unit {
		BPS("bps"),

		X1_KBPS("Kbps", BigDecimal.valueOf(1000), BPS),
		X4_KBPS(X1_KBPS),
		X16_KBPS(X4_KBPS),
		x64_Kbps(X16_KBPS),
		X256_KBPS(x64_Kbps),
		// Mbps
		X1_MBPS("Mbps", X256_KBPS),
		X4_MBPS(X1_MBPS),
		X16_MBPS(X4_MBPS),
		X64_MBPS(X16_MBPS),
		X256_MBPS(X64_MBPS),
		// Gbps
		X1_GBPS("Gbps", X256_MBPS),
		X4_GBPS(X1_GBPS),
		X16_GBPS(X4_GBPS),
		X64_GBPS(X16_GBPS),
		X256_GBPS(X64_GBPS),
		// Tbps
		X1_TBPS("Tbps", X256_GBPS),
		X4_TBPS(X1_TBPS),
		X16_TBPS(X4_TBPS),
		X64_TBPS(X16_TBPS),
		X256_TBPS(X64_TBPS),
		// Pbps
		X1_PBPS("Pbps", X256_TBPS),
		X4_PBPS(X1_PBPS),
		X16_PBPS(X4_PBPS),
		X64_PBPS(X16_PBPS),
		X256_PBPS(X64_PBPS);

		private final String prettyName;
		private final BigDecimal submultipleCoefficient;
		private final Unit submultiple;

		Unit(String prettyName) {
			this.prettyName = prettyName;
			this.submultipleCoefficient = BigDecimal.ONE;
			this.submultiple = null;
		}

		Unit(Unit submultiple) {
			this.submultiple = submultiple;
			this.prettyName = null;
			this.submultipleCoefficient = new BigDecimal(4);
		}

		Unit(String prettyName, Unit submultiple) {
			this.prettyName = prettyName;
			this.submultipleCoefficient = new BigDecimal("3.90625");
			this.submultiple = submultiple;
		}

		Unit(String prettyName, BigDecimal submultipleCoefficient, Unit submultiple) {
			this.prettyName = prettyName;
			this.submultipleCoefficient = submultipleCoefficient;
			this.submultiple = submultiple;
		}
	}
}
