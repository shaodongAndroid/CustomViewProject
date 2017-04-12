package com.custom.view.project.puzzle.activity;


import com.custom.view.project.puzzle.PuzzleLayout;
import com.custom.view.project.puzzle.layout.EightPieceLayout;
import com.custom.view.project.puzzle.layout.FivePieceLayout;
import com.custom.view.project.puzzle.layout.FourPieceLayout;
import com.custom.view.project.puzzle.layout.NinePieceLayout;
import com.custom.view.project.puzzle.layout.OnePieceLayout;
import com.custom.view.project.puzzle.layout.PuzzleLayoutHelper;
import com.custom.view.project.puzzle.layout.SevenPieceLayout;
import com.custom.view.project.puzzle.layout.SixPieceLayout;
import com.custom.view.project.puzzle.layout.ThreePieceLayout;
import com.custom.view.project.puzzle.layout.TwoPieceLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-18.
 */
public class PuzzleUtil {
    private static final String TAG = "PuzzleUtil";

    private PuzzleUtil() {

    }

    public static PuzzleLayout getPuzzleLayout(int borderSize, int themeId) {
        switch (borderSize) {
            case 1:
                return new OnePieceLayout(themeId);
            case 2:
                return new TwoPieceLayout(themeId);
            case 3:
                return new ThreePieceLayout(themeId);
            case 4:
                return new FourPieceLayout(themeId);
            case 5:
                return new FivePieceLayout(themeId);
            case 6:
                return new SixPieceLayout(themeId);
            case 7:
                return new SevenPieceLayout(themeId);
            case 8:
                return new EightPieceLayout(themeId);
            case 9:
                return new NinePieceLayout(themeId);
            default:
                return new OnePieceLayout(themeId);
        }
    }

    public static List<PuzzleLayout> getAllPuzzleLayout() {
        List<PuzzleLayout> puzzleLayouts = new ArrayList<>();
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(2));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(3));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(4));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(5));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(6));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(7));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(8));
        puzzleLayouts.addAll(PuzzleLayoutHelper.getAllThemeLayout(9));
        return puzzleLayouts;
    }
}
