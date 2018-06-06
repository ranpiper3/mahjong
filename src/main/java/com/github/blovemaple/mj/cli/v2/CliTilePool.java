package com.github.blovemaple.mj.cli.v2;

import static com.github.blovemaple.mj.cli.v2.CliViewDirection.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.blovemaple.mj.cli.framework.component.CliPanel;
import com.github.blovemaple.mj.cli.framework.component.CliSpace;
import com.github.blovemaple.mj.object.Tile;

/**
 * @author blovemaple <blovemaple2010(at)gmail.com>
 */
public class CliTilePool extends CliPanel {
	private static final int CENTER_SPACE_WIDTH = 23, CENTER_SPACE_HEIGHT = 6;

	private final Map<CliViewDirection, CliTilePoolSide> sidesByDirection;

	public CliTilePool() {
		sidesByDirection = new EnumMap<>(CliViewDirection.class);
		CliSpace centerSpace = CliSpace.of(CENTER_SPACE_WIDTH, CENTER_SPACE_HEIGHT);
		addChild(centerSpace);

		CliTilePoolSide lowerPool = new CliTilePoolSide(LOWER);
		lowerPool.setLeft(() -> centerSpace.getLeft(this).orElseThrow(RuntimeException::new));
		lowerPool.setTop(() -> centerSpace.getBottom(this).orElseThrow(RuntimeException::new) + 1);
		sidesByDirection.put(LOWER, lowerPool);
		addChild(lowerPool);

		CliTilePoolSide rightPool = new CliTilePoolSide(RIGHT);
		rightPool.setBottom(() -> centerSpace.getBottom(this).orElseThrow(RuntimeException::new));
		rightPool.setLeft(() -> centerSpace.getRight(this).orElseThrow(RuntimeException::new) + 1);
		sidesByDirection.put(RIGHT, rightPool);
		addChild(rightPool);

		CliTilePoolSide upperPool = new CliTilePoolSide(UPPER);
		upperPool.setRight(() -> centerSpace.getRight(this).orElseThrow(RuntimeException::new));
		upperPool.setBottom(() -> centerSpace.getTop(this).orElseThrow(RuntimeException::new) - 1);
		sidesByDirection.put(UPPER, upperPool);
		addChild(upperPool);

		CliTilePoolSide leftPool = new CliTilePoolSide(LEFT);
		leftPool.setTop(() -> centerSpace.getTop(this).orElseThrow(RuntimeException::new));
		leftPool.setRight(() -> centerSpace.getLeft(this).orElseThrow(RuntimeException::new) - 1);
		sidesByDirection.put(LEFT, leftPool);
		addChild(leftPool);
	}

	public void view(CliViewDirection direction, List<Tile> tiles) {
		sidesByDirection.get(direction).view(tiles);
	}
}
