package com.github.blovemaple.mj.cli.v2;

import static com.github.blovemaple.mj.cli.v2.CliViewDirection.*;

import java.util.EnumMap;
import java.util.Map;

import com.github.blovemaple.mj.cli.framework.component.CliPanel;
import com.github.blovemaple.mj.object.MahjongTablePlayerView;
import com.github.blovemaple.mj.object.PlayerInfo;
import com.github.blovemaple.mj.object.PlayerLocation;

/**
 * @author blovemaple <blovemaple2010(at)gmail.com>
 */
public class CliTable extends CliPanel {
	private PlayerLocation selfLocation;

	private final CliTilePool pool;
	private final Map<CliViewDirection, CliTileWallSide> wallSides;
	private final Map<CliViewDirection, CliPlayerTiles> playerTileses;

	public CliTable() {
		pool = new CliTilePool();
		addChild(pool);

		wallSides = new EnumMap<>(CliViewDirection.class);
		for (var direction : CliViewDirection.values()) {
			var wallSide = new CliTileWallSide(direction);
			wallSides.put(direction, wallSide);
			addChild(wallSide);
		}

		playerTileses = new EnumMap<>(CliViewDirection.class);
		for (var direction : CliViewDirection.values()) {
			var playerTiles = new CliPlayerTiles(direction);
			playerTileses.put(direction, playerTiles);
			addChild(playerTiles);
		}

		for (var direction : CliViewDirection.values()) {
			var wallSide = wallSides.get(direction);
			var playerTiles = playerTileses.get(direction);
			switch (direction) {
			case LEFT:
				wallSide.setTop(() -> pool.getTop(this).orElse(0));
				wallSide.setRight(() -> pool.getLeft(this).orElse(0) - 2);
				playerTiles.setTop(() -> wallSide.getTop(this).orElse(0) + 2);
				playerTiles.setRight(() -> wallSide.getLeft(this).orElse(0) - 3);
				break;
			case RIGHT:
				wallSide.setBottom(() -> pool.getBottom(this).orElse(0));
				wallSide.setLeft(() -> pool.getRight(this).orElse(0) + 2);
				playerTiles.setBottom(() -> wallSide.getBottom(this).orElse(0) - 2);
				playerTiles.setLeft(() -> wallSide.getRight(this).orElse(0) + 3);
				break;
			case LOWER:
				wallSide.setLeft(() -> pool.getLeft(this).orElse(0));
				wallSide.setTop(() -> pool.getBottom(this).orElse(0) + 1);
				playerTiles.setLeft(() -> wallSide.getLeft(this).orElse(0) + 6);
				playerTiles.setTop(() -> wallSide.getBottom(this).orElse(0) + 2);
				break;
			case UPPER:
				wallSide.setRight(() -> pool.getRight(this).orElse(0));
				wallSide.setBottom(() -> pool.getTop(this).orElse(0) - 1);
				playerTiles.setRight(() -> wallSide.getRight(this).orElse(0) - 6);
				playerTiles.setBottom(() -> wallSide.getTop(this).orElse(0) - 2);
				break;
			default:
				throw new RuntimeException();
			}
		}
	}

	public PlayerLocation getSelfLocation() {
		return selfLocation;
	}

	public void setSelfLocation(PlayerLocation selfLocation) {
		this.selfLocation = selfLocation;
	}

	public void view(MahjongTablePlayerView table, PlayerInfo selfInfo) {
		for (var location : PlayerLocation.values()) {
			var direction = self().getLocationOf(selfLocation.getRelationOf(location));

			// view poll
			pool.view(direction, table.getPlayerInfo(location).getDiscardedTiles());

			// view wall
			wallSides.get(direction).view(table.getTileWall().getPiles(location));

			// view playerTiles
			if (location == selfLocation) {
				// self
				playerTileses.get(direction).view(selfInfo, selfInfo.getLastDrawedTile());
			} else {
				// others
				var playerInfo = table.getPlayerInfo(location);
				playerTileses.get(direction).view(playerInfo.getTileGroups(), playerInfo.getAliveTileSize());
			}
		}
	}
}
