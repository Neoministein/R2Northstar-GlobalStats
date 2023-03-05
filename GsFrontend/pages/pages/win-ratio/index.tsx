import { Column } from 'primereact/column';
import BackendService from '../../../demo/service/BackendService';
import { WinRatioBucket } from '../../../demo/service/BackendService';
import TopResultTable from '../../../demo/components/TopResultTable';

const ButtonDemo = () => {

    const winRatioBody = (column: WinRatioBucket) => {
        if(column?.ratio) {
            return column.ratio.toFixed(3) + "%";
        }

        return null;
    }

    return (
        <TopResultTable
            title="Top Player Kills"
            getGlobalRanking={(tags) => BackendService.getTopWinRatio(tags)}
            columns={
                [<Column header="Player Name" field="playerName" />,
                <Column header="Ratio" body={winRatioBody} />,
                <Column header="Wins"  field="filters.win" />,
                <Column header="Loses" field="filters.lose" />]}/>
    );
};

export default ButtonDemo;
