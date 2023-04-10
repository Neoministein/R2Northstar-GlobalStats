import { Column } from 'primereact/column';
import TopService from '../../../demo/service/TopService';
import { WinRatioBucket } from '../../../demo/service/TopService';
import TopResultTable from '../../../demo/components/TopResultTable';

const ButtonDemo = () => {

    const winRatioBody = (column: WinRatioBucket) => {
        if(column?.ratio) {
            return column.ratio + "%";
        }
        return null;
    }

    return (
        <TopResultTable 
            title="Top Player Kills" 
            getGlobalRanking={(tags) => TopService.getTopWinRatio(tags)}
            columns={
                [<Column header="Player Name" field="playerName" />,
                <Column header="Ratio" body={winRatioBody} />,
                <Column header="Wins"  field="filters.win" />,
                <Column header="Loses" field="filters.lose" />]}/> 
    );
};

export default ButtonDemo;